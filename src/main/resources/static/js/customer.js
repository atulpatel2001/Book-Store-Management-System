$(document).ready(function () {

         $('.add-cart').click(function () {
             var book_id = $(this).data('book_id');
             console.log(book_id)

             $.ajax({
                 type: 'POST',
                 url: '/Book-Store/purchase-book/add-to-cart',
                 data: {
                     book_id: book_id,
                 },
                 success: function (response) {
                    Swal.fire("Successfully Add To Cart","", "success");
//                    location.reload();
                 },
                 error: function (xhr, status, error) {
                   Swal.fire("Not Added To Cart","","error");
                 }
             });
         });
     });



//     cart qunatity update
function adjustQuantity(change,cartId) {
    var quantityInput = document.getElementById('form' + cartId);
        var newQuantity = parseInt(quantityInput.value) + change;
         quantityInput.value = newQuantity;
        $.ajax({
                        type: 'POST',
                        url: '/Book-Store/purchase-book/change-quantity',
                        data: {
                            quantity : newQuantity,
                            cartId:cartId
                        },
                        success: function (response) {
                                        location.reload();
                        },
                        error: function (xhr, status, error) {

                        }
                    });
}


//delete cart

 $(document).ready(function () {
         $('.delete-cart').click(function () {
             var cart_id = $(this).data('cart_id');
             // Send an AJAX request to update the user's role
             $.ajax({
                 type: 'POST',
                 url: '/Book-Store/purchase-book/delete-cart', // Replace with the actual URL
                 data: {
                     cart_id: cart_id,
                 },
                 success: function (response) {
                     // Handle success, e.g., display a success message
                    Swal.fire("Remove Books From Cart","", "success");
                     location.reload();
                 },
                 error: function (xhr, status, error) {
                     // Handle error, e.g., display an error message
                   Swal.fire("Something Went Wrong","","error");
                 }
             });
         });
     });


//profile page


 //show change password page
            $(document).ready(function(){

              $('#change-pass-button').click(function(){
                          $('#customer-profile').hide();
                           $('#pending-order').hide();
                          $('#change-password').show();

              })
      })


 //profile page
            $(document).ready(function(){

              $('#profile-button').click(function(){
                            $('#change-password').hide();
                              $('#pending-order').hide();
                          $('#customer-profile').show();


              })
      })




            $(document).ready(function(){
              $('#pending-order-button').click(function(){
                            $('#change-password').hide();
                          $('#customer-profile').hide();
                          $('#pending-order').show();


              })
      })



 //Category bar open
            $(document).ready(function(){
              $('#open-category-bar').click(function(){
                          $('.category-book').show();
              })
      })
