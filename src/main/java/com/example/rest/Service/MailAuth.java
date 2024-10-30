package com.example.rest.Service;

import com.example.rest.Model.TempUser;
import com.example.rest.Model.User;
import com.example.rest.Repository.UserRepository;
import com.example.rest.Repository.VerificationRepository;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;


@Service
public class MailAuth {

    private final VerificationRepository verificationRepository;
    private final UserService userService;

    public MailAuth(VerificationRepository verificationRepository, UserRepository userRepository, UserService userService) {
        this.verificationRepository = verificationRepository;
        this.userService = userService;
        this.timeCheck();
    }

    public String send(User user) {

        String login = "alihesenzade5000@gmail.com";
        String key = "nonp yzdv oxbm vrug";

        Properties p = System.getProperties();
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getDefaultInstance(p, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, key);
            }
        });
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int y = random.nextInt(10);
            code.append(y);
        }
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noReply<alihesenzade5000@gmail.com>"));
            msg.setSubject("Verification");
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            msg.setText(code + " is your verification code.");
            Transport.send(msg);
            verificationRepository.save(new TempUser(user.getEmail(), code.toString(), user.getUsername(), user.getPassword(),user.getRole()));
            return "Verification mail sent via email";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void timeCheck() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                List<TempUser> list = verificationRepository.findAll();
                for (TempUser tempUser : list) {
                    if (tempUser.getTimeLeft() <= 0) {
                        verificationRepository.delete(tempUser);
                    } else {
                        tempUser.setTimeLeft(tempUser.getTimeLeft() - 5);
                        verificationRepository.save(tempUser);
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
    }

    public String verify(String mail, String code) {
        if (verificationRepository.existsById(mail)) {
            TempUser tempUser = verificationRepository.getReferenceById(mail);
            if (tempUser.getCode().equals(code)) {
                tempUser.setStatus(true);
                verificationRepository.save(tempUser);
                return userService.createUser(new User(0, tempUser.getUsername(), tempUser.getRole(), tempUser.getPassword(), tempUser.getEmail()));
            } else {
                return "Wrong verification code";
            }
        }
        return "Verification mail not found";
    }
}
