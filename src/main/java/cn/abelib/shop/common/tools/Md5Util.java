package cn.abelib.shop.common.tools;




import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by abel on 2017/12/6.
 *  MD5工具类
 */

public class Md5Util {
    private static final String SALT = "12f23g1cry`13gqwcery";

    private static char[] sources= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    /**
     *  获取文件流 MD5, 似乎第二种方法性能要高上50%
     * @param fis
     * @param length
     * @return
     */
    public static String getMd5(FileInputStream fis, long length) {
        String value = null;
        try {
            MappedByteBuffer byteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static String getMd5(InputStream is) throws NoSuchAlgorithmException, IOException {
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        // convert the byte to hex format
        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }

    /**
     *  计算字符串的md5值
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }


    /**
     *  由前端密码生成一个数据库密码
     * @param formPass
     * @param salt
     * @return String
     */
    public static String dbPassword(String formPass, String salt){
        String inputPass = salt + formPass + SALT;
        return md5(inputPass);
    }

    public static String randSalt(int size){
        long seed = System.currentTimeMillis();
        Random random =new Random(seed);
        char [] salts = new char[size];
        int N= sources.length;
        int index;
        for (int i=0; i<size; i++) {
            index = random.nextInt(N);
            salts[i] = sources[index];
        }
        return new String(salts);
    }
}
