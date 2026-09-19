package ui;

import dto.SinhVienDTO;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Bảng màu, font và cấu hình giao diện dùng chung toàn app. */
public final class Theme {

    private Theme() {
    }

    // ----- màu nền / chữ -----
    public static final Color BG = new Color(0xF3F4F8);
    public static final Color TEXT = new Color(0x111827);
    public static final Color LABEL = new Color(0x374151);
    public static final Color MUTED = new Color(0x6B7280);
    public static final Color BORDER = new Color(0xE5E7EB);
    public static final Color ROW_LINE = new Color(0xF1F5F9);
    public static final Color HEADER_BG = new Color(0xF9FAFB);

    // ----- màu chủ đạo (indigo) -----
    public static final Color PRIMARY = new Color(0x4F46E5);
    public static final Color PRIMARY_HOVER = new Color(0x4338CA);
    public static final Color PRIMARY_DARK = new Color(0x3730A3);
    public static final Color PRIMARY_SOFT = new Color(0xEEF2FF);
    public static final Color PRIMARY_LINE = new Color(0xC7D2FE);

    // ----- nguy hiểm / vô hiệu -----
    public static final Color DANGER = new Color(0xDC2626);
    public static final Color DANGER_SOFT = new Color(0xFEF2F2);
    public static final Color DANGER_LINE = new Color(0xFCA5A5);
    public static final Color DISABLED_BG = new Color(0xF3F4F6);
    public static final Color DISABLED_FG = new Color(0x9CA3AF);

    // ----- thanh cuộn -----
    public static final Color SCROLL_THUMB = new Color(0xD1D5DB);
    public static final Color SCROLL_THUMB_HOVER = new Color(0x9CA3AF);

    // ----- xếp loại -----
    public static final Color GIOI_FG = new Color(0x15803D);
    public static final Color GIOI_BG = new Color(0xDCFCE7);
    public static final Color KHA_FG = new Color(0x1D4ED8);
    public static final Color KHA_BG = new Color(0xDBEAFE);
    public static final Color TB_FG = new Color(0xB45309);
    public static final Color TB_BG = new Color(0xFEF3C7);
    public static final Color YEU_FG = new Color(0xB91C1C);
    public static final Color YEU_BG = new Color(0xFEE2E2);
    public static final Color INFO = new Color(0x0EA5E9);

    public static Color rankFg(String xepLoai) {
        switch (xepLoai) {
            case SinhVienDTO.GIOI:
                return GIOI_FG;
            case SinhVienDTO.KHA:
                return KHA_FG;
            case SinhVienDTO.TRUNG_BINH:
                return TB_FG;
            default:
                return YEU_FG;
        }
    }

    public static Color rankBg(String xepLoai) {
        switch (xepLoai) {
            case SinhVienDTO.GIOI:
                return GIOI_BG;
            case SinhVienDTO.KHA:
                return KHA_BG;
            case SinhVienDTO.TRUNG_BINH:
                return TB_BG;
            default:
                return YEU_BG;
        }
    }

    // ----- font -----
    private static String family;

    public static Font font(int style, int size) {
        if (family == null) {
            Set<String> available = new HashSet<>(Arrays.asList(
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            family = Font.DIALOG;
            // Các font này đều hỗ trợ tiếng Việt tốt, ưu tiên từ trên xuống
            for (String f : new String[] { "Segoe UI", "SF Pro Text", "Helvetica Neue", "Inter", "Roboto", "Arial" }) {
                if (available.contains(f)) {
                    family = f;
                    break;
                }
            }
        }
        return new Font(family, style, size);
    }

    private static final DecimalFormat DIEM_FORMAT = new DecimalFormat("0.00",
            DecimalFormatSymbols.getInstance(Locale.US));

    /** Định dạng điểm: 8.5 -> "8.50" (luôn dùng dấu chấm cho khớp với DB). */
    public static String fmt(double diem) {
        return DIEM_FORMAT.format(diem);
    }

    /** Chọn Look&Feel + đồng bộ font cho toàn bộ Swing (kể cả hộp thoại). */
    public static void install() {
        try {
            try {
                // Nếu bạn thêm thư viện FlatLaf vào classpath thì giao diện sẽ đẹp hơn nữa
                Class.forName("com.formdev.flatlaf.FlatLightLaf");
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (ClassNotFoundException e) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception ignored) {
            // dùng Look&Feel mặc định
        }

        for (Object key : Collections.list(UIManager.getDefaults().keys())) {
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                Font old = (Font) value;
                UIManager.put(key, new FontUIResource(font(old.getStyle(), 13)));
            }
        }
    }
}
