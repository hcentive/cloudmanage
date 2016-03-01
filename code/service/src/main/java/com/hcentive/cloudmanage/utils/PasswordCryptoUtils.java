package com.hcentive.cloudmanage.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class PasswordCryptoUtils {
	
	public static final String encryptionPassword = "cloudmanage";
	
	public static String encryptPassword(String password){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionPassword);
		String encryptedPassword = textEncryptor.encrypt(password);
		return encryptedPassword;
	}
	
	public static String decryptPassword(String encryptedPassword){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionPassword);
		String decryptedPassword = textEncryptor.decrypt(encryptedPassword);
		return decryptedPassword;
	}
	
	public static void main(String[] args){
			String password = "password";
			if(args != null && args.length == 1){
				password = args[0];
			}
			String encryptPassword = encryptPassword(password);
			System.out.println(encryptPassword);
			System.out.println(decryptPassword(encryptPassword));
		}

}
