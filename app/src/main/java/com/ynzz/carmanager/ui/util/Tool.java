package com.ynzz.carmanager.ui.util;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    public static String tofirstLowerCase(String str) {

        if (str != null && str.length() > 0) {
            return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
        } else if (str != null && str.length() == 0) {
            return str.toLowerCase();
        } else {
            return str;
        }

    }

    // Toast
    public static void initToast(Context c, String title) {
        try {
            Toast.makeText(c, title, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 判断手机号码的正确性
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断车牌字母数字正确性
     *
     * @param numbers
     * @return
     */
    public static boolean isCarNumber(String numbers) {
//        Pattern p = Pattern.compile("^([a-zA-Z0-9])+$");
        Pattern p = Pattern.compile("^{1}[A-Z]{1}[A-Z_0-9]{5}$");
        Matcher m = p.matcher(numbers);
        return m.matches();
    }

    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email
     * @return 是否合法
     */
    public static boolean isEmail(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-zA-Z0-9]){1}([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /*
    * TextView 自动换行
    * */
    public static String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}
