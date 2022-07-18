    package org.example;

    import lombok.extern.slf4j.Slf4j;
    import org.example.pojo.User;
    import org.example.pojo.UserService;

    import java.util.concurrent.CompletableFuture;

    @Slf4j
    public class ReactiveDemo {


        public static void main(String[] args) {
            System.out.println("==============================Async Parallel==============================");
            User user = reactiveFlow();
            System.out.println(user);
            System.out.println("==============================Sequential==============================");
            User user1 = sequentialFlow();
            System.out.println(user1);


        }

        private static User reactiveFlow(){
            User user = new User();
            UserService userService= new UserService();
            long startTime = System.nanoTime();
            CompletableFuture<Void> userCompletableFuture = userService.getLoginAsync("abcd")
                    .thenApplyAsync(login -> {
                        printThread();
                        System.out.println("login is " + login);
                        user.setLogin(login);
                        return login;
                    })
                    .thenAcceptAsync(nil -> System.out.println("time Taken for process " + (System.nanoTime() - startTime) / 1_000_000_000.0))
                    .thenComposeAsync(login -> userService.getEducationAsync(user.getLogin().getUsername()))
                    .thenCombineAsync(userService.getExperienceAsync(user.getLogin().getUsername()), (education, experience) -> {
                        printThread();
                        System.out.println("time Taken for thenCombineAsync " + (System.nanoTime() - startTime) / 1_000_000_000.0);
                        System.out.println("Education Details are " + education);
                        System.out.println("Experience Details are " + experience);
                        user.setEducation(education);
                        user.setExperience(experience);
                        return user;
                    })
                    .thenAcceptAsync(nil -> System.out.println("time Taken for process " + (System.nanoTime() - startTime) / 1_000_000_000.0))
                    .thenComposeAsync(nil -> userService.getSalaryAsync(user.getExperience().getYears(), user.getEducation().getHighestDegree()))
                    .thenApplyAsync(salary -> {
                                printThread();
                                System.out.println("Salary Details are " + salary);
                                user.setSalary(salary);
                                return user;
                            }
                    )
                    .exceptionally(throwable -> handleException(throwable, user))
                    .thenAcceptAsync(nil -> System.out.println("time Taken for process " + (System.nanoTime() - startTime) / 1_000_000_000.0))
                    .thenComposeAsync(user1 -> userService.getPaymentAsync(user))
                    .thenCombineAsync(userService.getAddressAsync(user.getUsername()), (payment, address) -> {
                        printThread();
                        System.out.println("payment is " + payment);
                        user.setPayment(payment);
                        System.out.println("address is " + address);
                        user.setAddress(address);
                        return user;
                    })
                    .exceptionally(throwable -> handleException(throwable, user))
                    .thenAcceptAsync(futureUser -> System.out.println("-----------All actions are completed for user"))
                    .thenAcceptAsync(nil -> System.out.println("time Taken for process " + (System.nanoTime() - startTime) / 1_000_000_000.0));
            CompletableFuture.allOf(userCompletableFuture).join();
            return user;
        }

        private static User sequentialFlow(){
            User user = new User();
            UserService userService= new UserService();
            long startTime = System.nanoTime();
            CompletableFuture<Void> userCompletableFuture = userService.getLoginAsync("abcd")
                    .thenApplyAsync(login->{
                        printThread();
                        System.out.println("login is "+login);
                        user.setLogin(login);
                        return login;
                    })
                    .thenComposeAsync(login -> userService.getEducationAsync(login.getUsername()))
                    .thenApplyAsync(education -> {
                        printThread();
                        System.out.println("Education Details are "+education);
                        user.setEducation(education);
                        return user;
                    })

                    .thenComposeAsync(user1 -> userService.getExperienceAsync(user1.getUsername()))
                    .thenApplyAsync(experience -> {
                        printThread();
                        System.out.println("Experience Details are "+experience);
                        user.setExperience(experience);
                        return user;
                    })
                    .thenComposeAsync(nil-> userService.getSalaryAsync(user.getExperience().getYears(),user.getEducation().getHighestDegree()))
                    .thenApply( salary-> {
                        printThread();
                        System.out.println("Salary Details are "+salary );
                        user.setSalary(salary);
                        return user;
                    })
                    .exceptionally(throwable -> handleException(throwable, user))
                    .thenComposeAsync(userService::getPaymentAsync)
                    .thenApplyAsync( payment-> {
                        printThread();
                        System.out.println("payment is "+ payment);
                        user.setPayment(payment);
                        return user;
                    })
                    .thenComposeAsync(user1->userService.getAddressAsync(user.getUsername()))
                    .thenApplyAsync( address-> {
                        printThread();
                        System.out.println("address is "+address);
                        user.setAddress(address);
                        return user;
                    })
                    .exceptionally(throwable -> handleException(throwable, user))
                    .thenAcceptAsync(futureUser-> System.out.println("----------All actions are completed for user "))
                    .thenAcceptAsync(nil-> System.out.println("time Taken for whole process "+(System.nanoTime()-startTime)/ 1_000_000_000.0));
            CompletableFuture.allOf(userCompletableFuture).join();
            return user;
        }

        private static void printThread(){
            System.out.println(Thread.currentThread());
        }

        private static User handleException(Throwable throwable, User user){
            System.out.println(throwable);
            return user;
        }

    }
