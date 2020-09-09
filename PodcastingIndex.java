import java.security.MessageDigest;
import java.util.*;
import java.net.*;
import java.io.*; 

public class PodcastingIndex {
    
public static void main(String[] args) {
        System.out.println("running...");
		// ======== Required values ======== 
		// WARNING: don't publish these to public repositories or in public places!
		// NOTE: values below are sample values, to get your own values go to https://api.podcastindex.org 
		String apiKey = "ABC";
		String apiSecret = "ABC";
		// prep for crypto
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
calendar.clear();
        Date now = new Date();
        calendar.setTime(now);
		long secondsSinceEpoch = calendar.getTimeInMillis() / 1000L;
		String apiHeaderTime = ""+secondsSinceEpoch;
		System.out.println("secs="+secondsSinceEpoch);
		String data4Hash = apiKey + apiSecret + apiHeaderTime;
		// ======== Hash them to get the Authorization token ========
		String hashString = PodcastingIndex.SHA1(data4Hash);
		System.out.println("hashString="+hashString);
		// ======== Send the request and collect/show the results ======== 
		String query = "bastiat";		
		try
		{
			URL url = new URL ("https://api.podcastindex.org/api/1.0/search/byterm?q="+query);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			// not needed for now, maybe later so I keep it
			//con.setRequestProperty("Accept", "application/json"); 
			//con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("X-Auth-Date", apiHeaderTime);
			con.setRequestProperty("X-Auth-Key", apiKey);
			con.setRequestProperty("Authorization", hashString);
			con.setRequestProperty("User-Agent", "SuperPodcastPlayer/1.8");
			con.setDoOutput(true);
			con.setChunkedStreamingMode(0);
			int code = con.getResponseCode();
			System.out.println("response code="+code);	
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8")))
			{
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
			}
			catch(IOException e) {
  				e.printStackTrace();
			}	
			con.disconnect();		
		}
		catch(Exception e) {
  			e.printStackTrace();
		}		     
    }
    
    
public static String byteArrayToString(byte[] bytes) {
    StringBuilder buffer = new StringBuilder();
    for (byte b : bytes) {
        buffer.append(String.format(Locale.getDefault(), "%02x", b));
    }
    return buffer.toString();
}

public static String SHA1(String clearString)
{
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(clearString.getBytes("UTF-8"));
        return byteArrayToString(messageDigest.digest());
    } catch (Exception ignored) {
        ignored.printStackTrace();
        return null;
    }
}
    
        
}
