<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Registration</h1>
    <hr>

    <form id="registrationForm" enctype="multipart/form-data">
        <div class="form-group">
            <label for="name">Name Surname</label>
            <input type="text" class="form-control" required id="name" name="name" placeholder="Name and surname">
            <div id="nameError" style="color: red;"></div>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" required name="email" placeholder="Email">
            <div id="emailError" style="color: red;"></div>
        </div>
        <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <input type="text" class="form-control" id="phoneNumber" required name="phoneNumber" placeholder="Phone number">
            <div id="phoneNumberError" style="color: red;"></div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" required name="password" placeholder="Password">
            <div id="passwordError" style="color: red;"></div>
        </div>
        <div class="form-group">
            <label for="passwordConfirmation">Password Confirmation</label>
            <input type="password" class="form-control" id="passwordConfirmation" required name="passwordConfirmation" placeholder="Confirm your password">
            <div id="passwordConfirmationError" style="color: red;"></div>
        </div>
        <button type="submit" style="width: 100%" class="btn btn-dark">Sign up</button>
    </form>

    <script>
        $(document).ready(function() {
            $('#registrationForm').on('submit', function(event) {
                event.preventDefault();

                var formData = new FormData(this);

                $.ajax({
                    type: 'POST',
                    url: '/registration',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        alert('Registration successful!');
                        window.location.href = "/login";
                    },
                    error: function(response) {
                        $('#nameError').text('');
                        $('#emailError').text('');
                        $('#phoneNumberError').text('');
                        $('#passwordError').text('');
                        $('#passwordConfirmationError').text('');

                        if (response.responseJSON && response.responseJSON.errors) {
                            var errors = response.responseJSON.errors;
                            if (errors.name) {
                                $('#nameError').text(errors.name);
                            }
                            if (errors.email) {
                                $('#emailError').text(errors.email);
                            }
                            if (errors.phoneNumber) {
                                $('#phoneNumberError').text(errors.phoneNumber);
                            }
                            if (errors.password) {
                                $('#passwordError').text(errors.password);
                            }
                            if (errors.passwordConfirmation) {
                                $('#passwordConfirmationError').text(errors.passwordConfirmation);
                            }
                        }
                    }
                });
            });
        });
    </script>
</@t.template>
