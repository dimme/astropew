/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

/**
 *
 * @author jonsturk
 */
public class Util {
    public static int bytesToInt(byte[] b, int off, int len){
            int val = 0;
            int mult = 1;
            for (int i=off+len-1; i>=off; i--)
            {
                    int toadd = b[i];
                    if ( toadd < 0)
                    {
                            toadd = 256 + toadd;
                    }
                    val += toadd * mult;

                    mult *= 256;
            }

            return val;
    }

	public static long bytesToLong(byte[] b, int off, int len){
		long val = 0;
        int mult = 1;
        for (int i=off+len-1; i>=off; i--)
        {
                int toadd = b[i];
                if ( toadd < 0)
                {
                        toadd = 256 + toadd;
                }
                val += toadd * mult;

                mult *= 256;
        }

        return val;
    }
}
