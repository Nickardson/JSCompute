package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.javascript.api.fs.ComputerNetFileJSAPI;
import org.mozilla.javascript.NativeObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;

public class APINet {
    public static APINetJSAPI create(IComputer computer) {
        return new APINetJSAPI(computer);
    }

    public static class APINetJSAPI {
        IComputer computer;

        private APINetJSAPI(IComputer computer) {
            this.computer = computer;
        }

        public ComputerNetFileJSAPI get(String url) {
            return get(url, null);
        }

        public ComputerNetFileJSAPI get(String url, NativeObject map) {
            try {
                String req = internalEncode(map);
                URLConnection connection = new URL(url + (req.length() > 0 ? "?" : "") + req).openConnection();

                InputStream stream = connection.getInputStream();
                return new ComputerNetFileJSAPI(computer, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String internalEncode(NativeObject map) {
            if (map == null) {
                return "";
            }

            StringBuilder builder = new StringBuilder();

            boolean first = true;
            for (Map.Entry<Object, Object> o : map.entrySet()) {
                if (!first) {
                    builder.append('&');
                }
                first = false;

                try {
                    builder.append(o.getKey().toString()).append("=").append(URLEncoder.encode(o.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException ignored) {
                }
            }

            return builder.toString();
        }

        /**
         * Encodes a parameter map to a URL-acceptable string.
         * @param map The javascript map object.
         * @return The formatted string.
         */
        public Object encode(NativeObject map) {
            return computer.convert(internalEncode(map));
        }

        public ComputerNetFileJSAPI post(String url) {
            return post(url, null);
        }

        public ComputerNetFileJSAPI post(String url, NativeObject object) {
            try {
                String data = internalEncode(object);

                URL u = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                {
                    os.writeBytes(data);
                    os.flush();
                }
                os.close();

                InputStream stream = conn.getInputStream();
                return new ComputerNetFileJSAPI(computer, stream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
