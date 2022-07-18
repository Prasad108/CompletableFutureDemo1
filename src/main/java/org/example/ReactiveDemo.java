    package reactive;

    import reactive.pojo.Salary;
    import reactive.pojo.User;
    import reactive.pojo.UserService;

    public class ReactiveDemo {


        public static void main(String[] args) throws InterruptedException {
            User user = new User();
            UserService userService= new UserService();

            userService.getLoginAsync("abcd")
                    .thenApplyAsync(login->{
                        System.out.println("login is "+login);
                        user.setLogin(login);
                        return login;
                    })
                    .thenCompose(login -> userService.getEducationAsync(login.getUsername())
                              .thenAcceptBoth(userService.getExperienceAsync(login.getUsername()), (education,experience)->{
                                  System.out.println("Education Details are "+education);
                                  System.out.println("Experience Details are "+experience);
                                  user.setEducation(education);
                                  user.setExperience(experience);
                              })
                    )
                    .thenCompose(nil-> userService.getSalaryAsync(user.getExperience().getYears(),user.getEducation().getHighestDegree()))
                    .thenApply( salary-> {
                        System.out.println("Salary Details are "+salary );
                        user.setSalary(salary);
                        return user;
                    }
                    )
                    .thenCompose(user1->userService.getPaymentAsync(user1))
                    .thenCombine(userService.getAddressAsync(user.getUsername()),(payment,address)->{
                        System.out.println("payment is "+ payment);
                        user.setPayment(payment);
                        System.out.println("address is "+address);
                        user.setAddress(address);
                        return user;
                    })
                    .thenAccept(futureUser-> System.out.println("All actions are completed for user "+user));
        Thread.sleep(1000);

        }
    }
