package reactive.pojo;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService {

    public CompletableFuture<Login> getLoginAsync(String username){
        sleep();
        return CompletableFuture.supplyAsync(()->Login.builder().username("username")
                .pwdHash("8743b52063cd84097a65d1633f5c74f5")
                .build()
        );
    }

    public CompletableFuture<Address> getAddressAsync(String username){
        sleep();
        return CompletableFuture.supplyAsync(()->Address.builder().houseName("username")
                .line1("xyz street")
                .line2("pqr lane")
                .state("barmuda")
                .district("13")
                .build()
        );
    }

    public CompletableFuture<Education> getEducationAsync(String username){
        sleep();
        return CompletableFuture.supplyAsync(()->Education.builder()
                .certificate("java 8")
                .highestDegree("BE")
                .build()
        );
    }

    public CompletableFuture<Experience> getExperienceAsync(String username){
        sleep();
        return CompletableFuture.supplyAsync(()->Experience.builder()
                .years(5)
                .companies(Arrays.asList(new String[] {"Google", "Meta", "Apple", "Amazon", "Netflix"}))
                .build()
        );
    }

    public CompletableFuture<Salary> getSalaryAsync(int years, String degree){
        sleep();
        return CompletableFuture.supplyAsync(()->Salary.builder()
                .amount(10000)
                .build()
        );
    }

    public CompletableFuture<Payment> getPaymentAsync(User user){
        sleep();
        return CompletableFuture.supplyAsync(()->Payment.builder()
                .account(3213435)
                .accountType("Saving")
                .build()
        );
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(100);
    }
}
