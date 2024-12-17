package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.EmailDetails;
import com.app_bank.app_bank.dto.StatementRequest;
import com.app_bank.app_bank.entity.Transaction;
import com.app_bank.app_bank.entity.User;
import com.app_bank.app_bank.repository.TransactionRepository;
import com.app_bank.app_bank.repository.UserRepository;
import com.app_bank.app_bank.utils.AccountUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class BankStatement {

    private EmailService emailService;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private static final String FILE = "C:\\Users\\Public\\Downloads\\MyStatement.pdf";

    public List generateStatement(StatementRequest statementRequest) throws FileNotFoundException, DocumentException {

        LocalDateTime startDate = LocalDate.parse(statementRequest.getStartDate(), DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(statementRequest.getEndDate(), DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);
        List<Transaction> transactionList = this.transactionRepository.findByAccountNumberAndCreateAtBetween(statementRequest.getAccountNumber(), startDate, endDate);
        User user = this.userRepository.findByAccountNumber(statementRequest.getAccountNumber());

        //Création et stylisation du document pdf
        designStatement(transactionList, user, startDate, endDate);


        return transactionList;
    }

    public void designStatement(List<Transaction> transactionList, User user, LocalDateTime startDate, LocalDateTime endDate) throws FileNotFoundException, DocumentException {

        String customerName = AccountUtils.fullNameUser(user);

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);

        log.info("Paramètrage de la taille du document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("BegginnerCoder21 Banque"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GRAY);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Cocody 2 plateaux vallon"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Date de debut: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("Statistique du compte"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("Date de fin: " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Nom du client: " + customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Adresse du client: " + user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.GRAY);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TYPE DE TRANSACTION"));
        transactionType.setBackgroundColor(BaseColor.GRAY);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("MONTANT DE LA TRANSACTION"));
        transactionAmount.setBackgroundColor(BaseColor.GRAY);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUT"));
        status.setBackgroundColor(BaseColor.GRAY);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionTable.addCell(transaction.getAccountNumber());
            transactionTable.addCell(transaction.getTransactionType());
            transactionTable.addCell(transaction.getAmount().toString());
            transactionTable.addCell(transaction.getStatus());
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);
        document.close();

        //Envoi de mail avec le fichier pdf des statistiques
        this.emailService.sendMailWithAttachment(EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATISTIQUE DU COMPTE UTILISATEUR")
                .mailBody("Récevez par ce mail les statistiques de toutes vos transactions !")
                .attachment(FILE)
                .build());

    }
}
