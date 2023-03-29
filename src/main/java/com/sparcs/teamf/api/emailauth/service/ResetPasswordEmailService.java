package com.sparcs.teamf.api.emailauth.service;

import com.sparcs.teamf.api.emailauth.exception.EmailSenderException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final String SUBJECT = "비밀번호 찾기 이메일 인증";

    @Override
    @Async
    public void send(String email, int verificationCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.addRecipients(RecipientType.TO, email);
            mimeMessage.setSubject(SUBJECT);
            mimeMessage.setText(generateEmailContent(verificationCode), "utf-8", "html");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("MessagingException : {}", e);
            throw new EmailSenderException();
        }
    }

    private String generateEmailContent(int emailVerificationCode) {
        StringBuffer content = new StringBuffer();
        return content.append(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "\n"
                + "<head>\n"
                + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                + "    <title>Email boilerplate</title>\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n"
                + "</head>\n"
                + "\n"
                + "<body style=\"margin: 0; padding: 0; background-color: #f0f0f5;\">\n"
                + "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"380\" style=\"border-collapse: collapse; margin-top: 50px; margin-bottom: 50px;\">\n"
                + "        <tr>\n"
                + "            <td valign=\"top\" style=\"padding-top: 20px;\">\n"
                + "                <img src=\"https://user-images.githubusercontent.com/62706048/227176823-38540711-c739-449a-b313-9abddca1e362.png\" alt=\"effective tech interview logo\" style=\"width: 28px; height: 24px\" />\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr valign=\"top\">\n"
                + "            <td valign=\"top\" style=\"vertical-align: bottom; height: 50px;\">\n"
                + "                <h1 style=\"\n"
                + "          font-size: 24px;\n"
                + "          color: #383c45;\n"
                + "        \">\n"
                + "                    회원가입 인증코드\n"
                + "                </h1>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td valign=\"top\" style=\"font-size: 14px; height: 80px; vertical-align: middle; line-height: 1.5; padding-bottom: 10px; color: #383c45;\">\n"
                + "                안녕하세요. 이펙티브 기술면접에 가입해주셔서 감사합니다.\n"
                + "                <br>\n"
                + "                서비스 가입을 위해 아래 인증번호를 화면에 입력해주세요.\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td valign=\"top\">\n"
                + "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"380\" style=\"border-collapse: collapse;\n"
                + "\tborder-radius: 50px;\">\n"
                + "                    <tr>\n"
                + "                        <td style=\"padding: 10px; font-size: 34px; background-color: #ffffff; text-align: center; padding-left: 34px;\">\n"
                + "                            <h1 style=\"font-size: 34px; line-height: 40px; letter-spacing: 24px; color: #000000; \">\n"
                +                                   emailVerificationCode
                + "                            </h1>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td style=\"height: 80px;\">\n"
                + "                <p style=\"\n"
                + "            margin: 0;\n"
                + "            margin-bottom: 40px;\n"
                + "            font-weight: 500;\n"
                + "            font-size: 13px;\n"
                + "            line-height: 18px;\n"
                + "            text-align: center;\n"
                + "            letter-spacing: -0.25px;\n"
                + "            color: #9ca7c0;\n"
                + "          \">\n"
                + "                    인증번호 유효시간 5분\n"
                + "                </p>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td style=\"border-top: 1px solid #9ca7c0; padding-top: 20px; padding-bottom: 20px; font-size: 11px; color: #9ca7c0; line-height: 1.5;\">\n"
                + "\n"
                + "                해당 이메일로 가입을 시도한 적이 없다면 이메일 계정 정보를 변경해주세요.\n"
                + "                본 메일은 발신 전용으로 회신되지 않습니다.\n"
                + "                <br>\n"
                + "                문의 : effectivetechinterview@gmail.com\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "</body>\n"
                + "\n"
                + "</html>").toString();
    }
}
