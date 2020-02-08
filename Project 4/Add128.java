import java.util.Random;

public class Add128 implements SymCipher {

	private byte [] key = new byte[128];
	
	public Add128() { new Random().nextBytes(key); }
	
	public Add128(byte[] byteKey) { key = byteKey; }

	@Override
	public byte[] getKey() { return key; }

	@Override
	public byte[] encode(String S) {
		
		byte [] s = S.getBytes();
		
		System.out.println("-----Encoding-----\nString: " + S);
		System.out.print("Decoded: ");
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i] + " ");
		} System.out.println();
		
		for (int i = 0; i < s.length; i++) { s[i] += key[i % key.length]; }
		
		System.out.print("Encoded: ");
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i] + " ");
		} System.out.println();
		
		return s;
	}

	@Override
	public String decode(byte[] bytes) {
		
		System.out.print("-----Decoding-----\nEncoded: ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i] + " ");
		} System.out.println();
		
		for (int i = 0; i < bytes.length; i++) { bytes[i] -= key[i % key.length]; }
		
		System.out.print("Decoded: ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i] + " ");
		} System.out.println();
		
		String S = new String(bytes);
		System.out.println("String: " + S);
		
		return S;
	}

}
