package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptografiaUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Criptografa a senha antes de salvar
    public static String hashSenha(String senhaPura) {
        if (senhaPura == null || senhaPura.isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia.");
        }
        if (senhaPura.length() > 72) {
            throw new IllegalArgumentException("A senha não pode ultrapassar 72 caracteres (limite do BCrypt).");
        }
        return encoder.encode(senhaPura);
    }

    // Verifica se a senha informada bate com o hash armazenado
    public static boolean verificarSenha(String senhaPura, String hashArmazenado) {
        return encoder.matches(senhaPura, hashArmazenado);
    }

    // Teste rápido
    public static void main(String[] args) {
        String senha = "123456";
        String hash = hashSenha(senha);

        System.out.println("Senha original: " + senha);
        System.out.println("Hash gerado: " + hash);
        System.out.println("Senha confere? " + verificarSenha("123456", hash));
    }
}

