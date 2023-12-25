 //show change password page
            $(document).ready(function(){
              $('#change-pass-button ').click(function(){
                          $('#staff-profile').hide();
                          $('#change-password').show();

              })
      })

 //profile page
            $(document).ready(function(){

              $('#profile-button').click(function(){
                            $('#change-password').hide();
                          $('#staff-profile').show();


              })
      })
//show otp form
//profile page
            $(document).ready(function(){



              $('.open-otp-form').click(function(){
                            var emailId = $(this).data('email-id');
                            var orderId = $(this).data('order-id');
                           $(".loader").show();
                               var dynamic = 'wait-' +$.escapeSelector(orderId);
                                         $('.' + dynamic).show();

                            $.ajax({
                                           type: 'POST',
                                           url: '/Book-Store/staff/generateOtp', // Replace with the actual URL
                                           data: {
                                               emailId:emailId
                                           },
                                           success: function (response) {
                                                                     $(".loader").hide();
                                                           $('.' + dynamic).hide();
                                              var dynamicClassName = 'otp-form-' +$.escapeSelector(orderId);
                                              $('.' + dynamicClassName).show();
                                           },
                                           error: function (xhr, status, error) {
                                               // Handle error, e.g., display an error message
                                             Swal.fire("Order is Completed","","error");
                                           }
                                       });
              })
      })
