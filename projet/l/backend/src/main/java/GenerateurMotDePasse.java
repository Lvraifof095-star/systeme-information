import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateurMotDePasse {
    public static void main(String[] args) {
        String motDePasseEnClair = "admin123"; // changez-le si besoin
        String hash = new BCryptPasswordEncoder().encode(motDePasseEnClair);
        System.out.println("Mot de passe en clair : " + motDePasseEnClair);
        System.out.println("Hash a copier en base  : " + hash);
    }
}