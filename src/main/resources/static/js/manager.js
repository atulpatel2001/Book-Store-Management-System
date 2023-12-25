  //for book----------------------------------

       //show add book form
            $(document).ready(function(){

              $('.add-book-button').click(function(){
                          $('.show-books').hide();
                          $('#add-books').show();

              })
      })

      //show all books
      $(document).ready(function(){

              $('.show-book-button').click(function(){
                          $('#add-books').hide();
                           $('.show-books').show();

              })
      })

// delete book

  $(document).ready(function () {
                          $('.delete-book').click(function () {

                              var book_id = $(this).data('book_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Book.',
                                  icon: 'warning',
                                  showCancelButton: true,
                                  confirmButtonText: 'Yes, delete it!',
                                  cancelButtonText: 'No, cancel!',
                                  reverseButtons: true,
                              }).then((result) => {
                                  if (result.isConfirmed) {
                                      // User confirmed, send an AJAX request to delete the user
                                      $.ajax({
                                          type: 'POST',
                                          url:'/Book-Store/manager/delete-book', // Replace with the actual URL
                                          data: {
                                              book_id: book_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The Book has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the book.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The book has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });




$(document).ready(function(){

        $('.add-staff-button').click(function(){
                    $('.show-staff').hide();
                    $('#add-staff').show();

        })
})

//show all Staff
$(document).ready(function(){

        $('.show-staff-button').click(function(){
                    $('#add-staff').hide();
                     $('.show-staff').show();

        })
})



// delete staff

  $(document).ready(function () {
                          $('.delete-staff').click(function () {

                              var staff_id = $(this).data('staff_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Staff.',
                                  icon: 'warning',
                                  showCancelButton: true,
                                  confirmButtonText: 'Yes, delete it!',
                                  cancelButtonText: 'No, cancel!',
                                  reverseButtons: true,
                              }).then((result) => {
                                  if (result.isConfirmed) {
                                      // User confirmed, send an AJAX request to delete the user
                                      $.ajax({
                                          type: 'POST',
                                          url:'/Book-Store/manager/delete-staff', // Replace with the actual URL
                                          data: {
                                              staff_id: staff_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The staff has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the staff.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The staff has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });


//change status



//user change role
  $(document).ready(function () {
         $('.change-status').change(function () {
             var status = $(this).val();
             var orderId = $(this).data('order-id');
//             $("#loader").show();
             var dynamic = 'wait-' +$.escapeSelector(orderId);
              $('.' + dynamic).show();
             $.ajax({
                 type: 'POST',
                 url: '/Book-Store/manager/updateChangeStatus', // Replace with the actual URL
                 data: {
                     status: status,
                     orderId: orderId
                 },
                 success: function (response) {
                     // Handle success, e.g., display a success message
                     $("#loader").hide();
                    Swal.fire("Status is Changed","", "success");
                    $('.' + dynamic).hide();
                 },
                 error: function (xhr, status, error) {
                     // Handle error, e.g., display an error message
                   Swal.fire("Status is not Changed","","error");
                 }
             });
         });
     });


 //profile page
            $(document).ready(function(){
              $('#profile-button').click(function(){
                            $('#change-password').hide();
                          $('#manager-profile').show();
              })
      })


            $(document).ready(function(){
              $('#change-pass-button').click(function(){
                $('#manager-profile').hide();
                 $('#change-password').show();



              })
      })