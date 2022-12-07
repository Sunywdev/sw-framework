package com.sw.xyz.springframework.core.sensitive;


import org.apache.commons.lang3.StringUtils;

/**
 * @author : sunyw
 * @date : 2022-05-24
 * @function:
 **/
public class SensitiveUtils {
    /*
     *中文名脱敏 保留首位
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return fullName;
        }
        String name=StringUtils.left(fullName,1);
        return StringUtils.rightPad(name,StringUtils.length(fullName),"*");
    }

    /*
     *身份证号脱敏 前6后4
     */
    public static String idCardNum(String idCardNum) {
        if (StringUtils.isBlank(idCardNum)) {
            return idCardNum;
        }
        return StringUtils.left(idCardNum,6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idCardNum,4),StringUtils.length(idCardNum)
                ,"*"),"******"));
    }

    /*
     *手机号脱敏 前3后4
     */
    public static String phoneNo(String phoneNo) {
        if (StringUtils.isBlank(phoneNo)) {
            return phoneNo;
        }
        return StringUtils.left(phoneNo,3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phoneNo,4),StringUtils.length(phoneNo)
                ,"*"),"***"));
    }

    /*
     *银行卡号脱敏 前6后4
     */
    public static String bankCard(String bankCard) {
        if (StringUtils.isBlank(bankCard)) {
            return bankCard;
        }
        return StringUtils.left(bankCard,6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(bankCard,4),StringUtils.length(bankCard)
                ,"*"),"******"));
    }

    /*
     * 邮箱脱敏,替换@前三位
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)||!email.contains("@")) {
            return email;
        }
        int atIndex=email.indexOf("@");
        int startIndex=atIndex - 3;
        if (0>startIndex){
            return email;
        }
        String consMail = email.substring(startIndex,startIndex+3);
        return StringUtils.replace(email,consMail,"***");
    }
}
