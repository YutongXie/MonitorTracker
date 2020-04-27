package com.huitong.monitortracker.processor.customize.alert;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachConstants;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.AlertProcessor;
import com.huitong.monitortracker.utils.MonitorTrackerConfigurationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NumberUpperLimitBreachAlertProcessor implements AlertProcessor {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Value("${global.env}")
    private String env = "UNKNOWN";

    public void sendEmail(MonitorTrackerJobDetailConfig config, List<NumberUpperLimitBreachResult> alertData) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            String to = StringUtils.trimToEmpty(config.getValue1());
            String subject = transformSubject(StringUtils.trimToEmpty(config.getValue4()));
            String cc = StringUtils.trimToEmpty(config.getValue2());
            String template = StringUtils.trimToEmpty(config.getValue3());
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            if(StringUtils.isNotBlank(cc)) {
                helper.setCc(cc);
            }
            Context context = new Context();
            context.setVariable("result", alertData);
            String body = templateEngine.process(template, context);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException ex) {
            log.error("failed to send alert email.", ex);
        }
    }

    @Override
    public void execute(MonitorTrackerJobDetailConfig config) {
        if(!isValidConfig(config)) {
            log.error("Invalid alert config - {}", config);
        }
        List<NumberUpperLimitBreachResult> alertData = extractAlertData(getInputData());
        if(CollectionUtils.isEmpty(alertData)) {
            log.warn("No alert data to report...");
        }
        sendEmail(config, alertData);
    }

    private List<NumberUpperLimitBreachResult> getInputData() {
        List<NumberUpperLimitBreachResult> inputData = new ArrayList<>();
        Object obj = ExecutorThreadLocal.getInputData();
        if(obj instanceof List) {
            if(((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachMetaData) {
                inputData = (List<NumberUpperLimitBreachResult>) obj;
            }
        }
        return inputData;
    }

    private List<NumberUpperLimitBreachResult> extractAlertData(List<NumberUpperLimitBreachResult> inputData) {
        return inputData
                .stream()
                .filter(data -> NumberUpperLimitBreachConstants.WARNING_LINE_DAYS_REACH_TO_THRESHOLD_PERCENT.getValue().equalsIgnoreCase(data.getDaysReach80Percent())
                        || new BigDecimal(data.getUsePercent().replace("%", ""))
                        .compareTo(new BigDecimal(NumberUpperLimitBreachConstants.WARNING_LINE_THRESHOLD_USE_PERCENT.getValue())) > 0)
                .collect(Collectors.toList());
    }

    private boolean isValidConfig(MonitorTrackerJobDetailConfig config) {
        String to = config.getValue1();
        String cc = config.getValue2();
        String template = config.getValue3();
        String subject = config.getValue4();

        if(StringUtils.isBlank(to)) {
            return false;
        }

        if(StringUtils.isBlank(template)) {
            return false;
        }

        if(StringUtils.isBlank(subject)) {
            return false;
        }
        return true;
    }

    private String transformSubject(String subject) {
        String finalSubject = subject;
        //${ENV} - Number Upper Limit Breach Report - ${date-yyyy/MM/dd}
        try {
            if(finalSubject.startsWith("${ENV}")) {
                finalSubject = finalSubject.replace("${ENV}", "[" + StringUtils.upperCase(env) + "]");
            }
            if(finalSubject.startsWith("${env}")) {
                finalSubject = finalSubject.replace("${ENV}", "[" + StringUtils.upperCase(env) + "]");
            }

            if(finalSubject.contains("${date-")) {
                int datePatternStartIndex = finalSubject.indexOf("${date-");
                String firstPart = finalSubject.substring(0, datePatternStartIndex);
                String dateStr = finalSubject.substring(datePatternStartIndex + 1);
                int datePatternEndIndex = dateStr.indexOf("}");
                String pattern = dateStr.substring(6, datePatternEndIndex);

                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(true);
                finalSubject = firstPart + sdf.format(new Date());
            }
        } catch (Exception e) {
            log.error("failed to transform email subject - {}", subject, e);
        } finally {
            return finalSubject;
        }
    }

    public static void main(String[] args) {
        NumberUpperLimitBreachAlertProcessor processor = new NumberUpperLimitBreachAlertProcessor();
        processor.transformSubject("${ENV} - Number Upper Limit Breach Report - ${date-yyyy/MM/dd}");
    }
}
