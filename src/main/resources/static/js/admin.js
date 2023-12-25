

 // for slide bar

 let list = document.querySelectorAll(".navigation li");
        function activeLink() {
          list.forEach((item) => {
            item.classList.remove("hovered");
          });
          this.classList.add("hovered");
        }

        list.forEach((item) => item.addEventListener("mouseover", activeLink));

        // Menu Toggle
        let toggle = document.querySelector(".toggle");
        let navigation = document.querySelector(".navigation");
        let main = document.querySelector(".main");

        toggle.onclick = function () {
          navigation.classList.toggle("active");
          main.classList.toggle("active");
        };

        // for book category----------------------------

      //show add category form
      $(document).ready(function () {
        $(".add-category-button").click(function () {
          $(".show-category").hide();
          $("#add-category").show();
        });
      });

      //show all categories
      $(document).ready(function () {
        $(".show-category-button").click(function () {
          $("#add-category").hide();
          $(".show-category").show();
        });
      });


 $(document).ready(function () {
                          $('.delete-category').click(function () {
                              var category_id = $(this).data('category_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Category.',
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
                                          url:'/Book-Store/admin/delete-book-category', // Replace with the actual URL
                                          data: {
                                              category_id: category_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The Book Category has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the Book Category.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The Category has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });




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
                                          url:'/Book-Store/admin/delete-book', // Replace with the actual URL
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


// manage Manager

$(document).ready(function(){

        $('.add-employee-button').click(function(){
                    $('.show-employee').hide();
                    $('#add-employee').show();

        })
})

//show all service
$(document).ready(function(){

        $('.show-employee-button').click(function(){
                    $('#add-employee').hide();
                     $('.show-employee').show();

        })
})



// delete Manager

  $(document).ready(function () {
                          $('.delete-employee').click(function () {

                              var employee_id = $(this).data('employee_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Manager.',
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
                                          url:'/Book-Store/admin/delete-manager', // Replace with the actual URL
                                          data: {
                                              employee_id: employee_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The Manager has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the Manager.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The Manager has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });



//=========staff==========='


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
                                          url:'/Book-Store/admin/delete-staff', // Replace with the actual URL
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


//change role
$(document).ready(function () {
         $('.change-role').change(function () {
             var selectedRole = $(this).val();
             var customerId  = $(this).data('customer-id');
             // Send an AJAX request to update the user's role
             $.ajax({
                 type: 'POST',
                 url:'/Book-Store/admin/change-role', // Replace with the actual URL
                 data: {
                     customerId: customerId,
                     newRole: selectedRole
                 },
                 success: function (response) {
                     // Handle success, e.g., display a success message
                    Swal.fire("Role is Updated","", "success");
                 },
                 error: function (xhr, status, error) {
                     // Handle error, e.g., display an error message
                   Swal.fire("Role is not Updated","","error");
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