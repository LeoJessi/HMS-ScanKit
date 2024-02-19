package top.jessi.scan.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

/**
 * Created by Jessi on 2024/2/19 11:51
 * Email：17324719944@189.cn
 * Describe：条形码生成
 */
public class Code {

    private static final String TAG = "HMS_Code";

    public static Bitmap generateQrCode(String content, int width, int height) {
        return generate(CodeType.QRCODE, content, width, height, 0, Color.BLACK, Color.WHITE, ErrorCorrection.H, null);
    }

    public static Bitmap generate(CodeType codeType, String content, int width, int height) {
        return generate(codeType, content, width, height, 0, Color.BLACK, Color.WHITE, ErrorCorrection.H, null);
    }

    /**
     * 生成二维码
     *
     * @param codeType        类型
     * @param content         内容
     * @param width           宽
     * @param height          高
     * @param margin          边距
     * @param codeColor       码的颜色
     * @param backgroundColor 背景颜色
     * @param errorCorrection 容错率
     * @param logo            中间logo
     * @return 生成二维码
     */
    public static Bitmap generate(CodeType codeType, String content, int width, int height, int margin, int codeColor,
                                  int backgroundColor, ErrorCorrection errorCorrection, Bitmap logo) {
        // 设置条形码的宽度和高度。
        if (content.length() == 0) {
            Log.w(TAG, "Failed to generate code -- Please input content first!");
            return null;
        }
        // 如果码的颜色与背景颜色相同也不能生成
        if (codeColor == backgroundColor) {
            Log.w(TAG, "Failed to generate code -- The color and background cannot be the same!");
            return null;
        }
        // 设置条形码的宽度和高度。
        if (width <= 0 || height <= 0) {
            width = 600;
            height = 600;
        }
        // 设置生成的条形码类型
        int type = switch (codeType) {
            case QRCODE -> HmsScan.QRCODE_SCAN_TYPE;
            case DATAMATRIX -> HmsScan.DATAMATRIX_SCAN_TYPE;
            case PDF417 -> HmsScan.PDF417_SCAN_TYPE;
            case AZTEC -> HmsScan.AZTEC_SCAN_TYPE;
            case EAN8 -> HmsScan.EAN8_SCAN_TYPE;
            case EAN13 -> HmsScan.EAN13_SCAN_TYPE;
            case UPC_A -> HmsScan.UPCCODE_A_SCAN_TYPE;
            case UPC_E -> HmsScan.UPCCODE_E_SCAN_TYPE;
            case CODABAR -> HmsScan.CODABAR_SCAN_TYPE;
            case CODE39 -> HmsScan.CODE39_SCAN_TYPE;
            case CODE93 -> HmsScan.CODE93_SCAN_TYPE;
            case CODE128 -> HmsScan.CODE128_SCAN_TYPE;
            case ITF -> HmsScan.ITF14_SCAN_TYPE;
        };
        // 设置容错率
        HmsBuildBitmapOption.ErrorCorrectionLevel errorCorrectionLevel = switch (errorCorrection) {
            case L -> HmsBuildBitmapOption.ErrorCorrectionLevel.L;
            case M -> HmsBuildBitmapOption.ErrorCorrectionLevel.M;
            case Q -> HmsBuildBitmapOption.ErrorCorrectionLevel.Q;
            case H -> HmsBuildBitmapOption.ErrorCorrectionLevel.H;
        };
        try {
            // 配置生成条形码
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator()
                    // 设置条形码边距
                    .setBitmapMargin(margin)
                    // 设置条形码的颜色
                    .setBitmapColor(codeColor)
                    // 设置条形码的背景颜色
                    .setBitmapBackgroundColor(backgroundColor)
                    // 设置容错率
                    .setQRErrorCorrection(errorCorrectionLevel)
                    // 设置中间logo
                    .setQRLogoBitmap(logo)
                    .create();
            return ScanUtil.buildBitmap(content, type, width, height, options);
        } catch (WriterException e) {
            Log.w(TAG, "Failed to generate code -- " + e.getMessage());
            return null;
        }
    }

    public enum CodeType {
        QRCODE,
        DATAMATRIX,
        PDF417,
        AZTEC,
        EAN8,
        EAN13,
        UPC_A,
        UPC_E,
        CODABAR,
        CODE39,
        CODE93,
        CODE128,
        ITF
    }

    public enum ErrorCorrection {
        // 7%
        L,
        // 15%
        M,
        // 25%
        Q,
        // 30%
        H
    }
}
