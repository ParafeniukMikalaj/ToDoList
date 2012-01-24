package com.todo.logic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.User;

@Component
public class SecurityLogic {
	
	private static ToDoDataProvider provider;

	@Resource(name="dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		SecurityLogic.provider = providerFactory.getDataProvider();
	}
	
	private static class SimpleSHA1 {

		private static String convertToHex(byte[] data) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < data.length; i++) {
				int halfbyte = (data[i] >>> 4) & 0x0F;
				int two_halfs = 0;
				do {
					if ((0 <= halfbyte) && (halfbyte <= 9))
						buf.append((char) ('0' + halfbyte));
					else
						buf.append((char) ('a' + (halfbyte - 10)));
					halfbyte = data[i] & 0x0F;
				} while (two_halfs++ < 1);
			}
			return buf.toString();
		}

		public static String SHA1(String text) {
			try {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA-1");
				byte[] sha1hash = new byte[40];
				md.update(text.getBytes("iso-8859-1"), 0, text.length());
				sha1hash = md.digest();
				return convertToHex(sha1hash);
			} catch (NoSuchAlgorithmException e) {
				/* nothing to do this couldn't happen */
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				/* nothing to do this couldn't happen */
				e.printStackTrace();
			}
			return text;
		}
	}

	public static String authorizeUser(String userName, String password) {
		ArrayList<User> users = provider.getAllUsers();
		for (User user : users) {
			if (user.getName().equals(userName)
					&& user.getPassword() == getHash(password))
				return user.getPassword();
		}
		return null;
	}

	public static boolean usernameAvailable(String userName) {
		ArrayList<User> users = provider.getAllUsers();
		for (User user : users) {
			if (user.getName().equals(userName))
				return false;
		}
		return true;
	}

	public static String getHash(String password) {
		return SimpleSHA1.SHA1(password);
	}
	
}
