import java.util.ArrayList;
import java.util.Collections;

public class Substitute implements SymCipher {

	byte []  key = new byte[256];
	byte [] akey = new byte[256];

	public Substitute() {

		ArrayList<Byte> byteA = new ArrayList<Byte>();
		for (int i = 0; i < 256; i++) { byteA.add((byte) i); }
		Collections.shuffle(byteA);

		for (int i = 0; i < 256; i++) {
			key[i] = byteA.get(i);
			akey[(int) key[i] & 0xFF] = (byte) i;
		}

	}

	public Substitute(byte[] byteKey) { 
		
		key = byteKey.clone(); 
		
		for (int i = 0; i < 256; i++) {
			akey[(int) key[i] & 0xFF] = (byte) i;
		}
		
	}

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
		
		for (int i = 0; i < s.length; i++) { s[i] = key[Byte.toUnsignedInt(s[i])]; }
		
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
		
		for (int i = 0; i < bytes.length; i++) { bytes[i] = akey[Byte.toUnsignedInt(bytes[i])]; }
		
		System.out.print("Decoded: ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i] + " ");
		} System.out.println();
		
		String S = new String(bytes);
		System.out.println("String: " + S);
		
		return S;
	}

}
