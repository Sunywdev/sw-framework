package com.sw.xyz.springframework.core.sensitive;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.lang.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : sunyw
 * @date : 2022-05-24
 * @function:
 **/
public class LogPatternLayout extends PatternLayout {


    private static final Map<PatternType, Pattern> patternMap = new ConcurrentHashMap<>();
    private static final String KEY = "**********";
    public LogPatternLayout(){
        loadPatterns();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = super.doLayout(event);
        if (CollectionUtils.isEmpty(patternMap)){
            loadPatterns();
        }
        try {
            return process(message);
        }catch (Exception e){
            return message;
        }
    }

    private void loadPatterns(){
        for (PatternType value : PatternType.values()) {
            Pattern compile = Pattern.compile(value.getRegex());
            patternMap.put(value,compile);
        }
    }


    public static String process(String message){
        for (PatternType patternType : patternMap.keySet()) {
            Pattern pattern = patternMap.get(patternType);
            Matcher matcher = pattern.matcher(message);
            Set<String> matches = exMatchByType(matcher);
            if (!CollectionUtils.isEmpty(matches)){
                message=maskByType(patternType,message,matches);
            }
        }
        return message;
    }

    private static Set<String> exMatchByType(Matcher matcher){
        Set<String> matches = new HashSet<>();
        int count = matcher.groupCount();
        while (matcher.find()){
            if (count==0){
                matches.add(matcher.group());
                continue;
            }
            for (int i = 0; i <= count; i++) {
                String match = matcher.group();
                if (null!=match){
                    matches.add(match);
                }
            }
        }
        return matches;
    }

    private static String maskByType(PatternType key,String message,Set<String>matches){
        if (key.equals(PatternType.ID_CARD)){
            return maskIdCard(message,matches);
        }else if (key.equals(PatternType.BANK_CARD)){
            return maskBankCard(message,matches);
        }else if (key.equals(PatternType.PHONE_NUMBER)){
            return maskPhone(message,matches);
        }else{
            return message;
        }
    }

    private static String maskIdCard(String message,Set<String>matches){
        for (String match : matches) {
            String matchProcess = baseSensitive(match,6,4);
            message=message.replace(match,matchProcess);
        }
        return message;
    }

    private static String maskBankCard(String message,Set<String>matches){
        for (String match : matches) {
            String matchProcess = baseSensitive(match,6,4);
            message=message.replace(match,matchProcess);
        }
        return message;
    }

    private static String maskPhone(String message,Set<String>matches){
        for (String match : matches) {
            String matchProcess = baseSensitive(match,3,4);
            message=message.replace(match,matchProcess);
        }
        return message;
    }

    private static String baseSensitive(String str,int startLength,int endLength){
        if (Validator.isEmpty(str)){
            return str;
        }
        String replaceMent=str.substring(startLength,str.length()-endLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < replaceMent.length(); i++) {
            char ch;
            if (replaceMent.charAt(i)>='0'&&replaceMent.charAt(i)<='9'){
                ch = KEY.charAt(replaceMent.charAt(i)-'0');
            }else{
                ch =replaceMent.charAt(i);
            }
            sb.append(ch);
        }
        return StringUtils.left(str,startLength).concat(StringUtils.leftPad(StringUtils.right(str,endLength),str.length()-startLength,sb.toString()));
    }

    @Getter
    @AllArgsConstructor
    private enum PatternType{

        PHONE_NUMBER("手机号","[^\\d](1[3456789]\\d{9}[^\\d])"),
        BANK_CARD("银行卡","[^\\d](\\d{16})[^\\d]|[^\\d](\\d{19})[^\\d]"),
        ID_CARD("身份证","[^\\d](\\d{15})[^\\d]|[^\\d](\\d{18})[^\\d]|[^\\d](\\d{17}X)");
        private String description;
        private String regex;
    }
}
