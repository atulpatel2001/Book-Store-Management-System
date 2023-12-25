//for single book
const singleBookPayment = (book_id,amount,quantity) => {
              $("#loader").show();
    $.ajax({
        url: "/Book-Store/payment/create_order_single",
        data: JSON.stringify({ amount: amount,quantity:quantity, info: 'order_request'}),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
        success: function (response) {

        console.log(response)
            //success
            console.log(response.status);

            if (response.status == "created") {
                //open payment form
                let options = {
                    key: "rzp_test_ADEnLyqI9oALQY",
                    amount: response.amount,
                    currency: "INR",
                    name: "Online Book Store",
                    description:
                        "our donation will directly impact the lives of impoverished children, providing them with vital support and opportunities they deserve. With your generosity, we can:",
                    image:
                        "https://www.savethechildren.in/wp-content/uploads/2022/08/Artboard-2-1.webp",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        updatePaymentOnServerSingleBook(response.razorpay_payment_id,response.razorpay_order_id,book_id,quantity);


                    },
                    prefill: {
                        name: "",
                        email: "",
                        contact: ""
                    },
                    notes: {
                        address: "AT POST Utility Service Provider"
                    },
                    theme: {
                        color: "#3399cc"
                    }

                };
                let razorpay = new Razorpay(options);
                razorpay.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    // window.alert("oops!! payment failed");
                    Swal.fire("Failed!!", "Opps!!!payment failed", "error");
                });
                razorpay.open();
                 $("#loader").hide();

            }



        },
        error: function (erro) {

        Swal.fire("Failed!!", "Opps!!! Something Went Wrong", "error");
         $("#loader").hide();
            //error
            console.log(erro);

        }

    })

};


//update payment



function updatePaymentOnServerSingleBook(paymentId, orderId,book_id,quantity) {
      $("#loader").show();
    $.ajax({
        url: '/Book-Store/payment/update_singleBookPayment',
        data: JSON.stringify({ payment_id: paymentId, order_id: orderId,book_id:book_id,quantity:quantity }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            Swal.fire("Congratulation", "Payment Successfully Completed !!", "success");
              $("#loader").hide();
            location.reload();
        },
        error: function (error) {
            Swal.fire("Failed", "Payment Successfully Completed,but we did not get on server,we will contact you as soon as possible", "warning");
        }
    })
}


















/*
//all payment


const allPayment=()=>{
$.ajax({
        url: "/Book-Store/payment/create_order_all",
        data: JSON.stringify({info: 'order_request'}),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
        success: function (response) {
            //success
            console.log(response.status);

            if (response.status == "created") {
                //open payment form
                let options = {
                    key: "rzp_test_ADEnLyqI9oALQY",
                    amount: response.amount,
                    currency: "INR",
                    name: "Online Book Store",
                    description:
                        "our donation will directly impact the lives of impoverished children, providing them with vital support and opportunities they deserve. With your generosity, we can:",
                    image:
                        "https://www.savethechildren.in/wp-content/uploads/2022/08/Artboard-2-1.webp",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        updatePaymentOnServerAllBook(response.razorpay_payment_id, response.razorpay_order_id);


                    },
                    prefill: {
                        name: "",
                        email: "",
                        contact: ""
                    },
                    notes: {
                        address: "AT POST Utility Service Provider"
                    },
                    theme: {
                        color: "#3399cc"
                    }

                };
                let razorpay = new Razorpay(options);
                razorpay.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    // window.alert("oops!! payment failed");
                    Swal.fire("Failed!!", "Opps!!!payment failed", "error");
                });
                razorpay.open();
                 $("#loader").hide();

            }



        },
        error: function (erro) {

        Swal.fire("Failed!!", "Opps!!! Something Went Wrong", "error");
         $("#loader").hide();
            //error
            console.log(erro);

        }

    })

}


//update all


//update payment



function updatePaymentOnServerAllBook(paymentId, orderId) {

    $.ajax({
        url: '/Book-Store/payment/update_doubleBookPayment',
        data: JSON.stringify({ payment_id: paymentId, order_id: orderId}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            Swal.fire("Congratulation", "Payment Successfully Completed !!", "success");
        },
        error: function (error) {
            Swal.fire("Failed", "Payment Successfully Completed,but we did not get on server,we will contact you as soon as possible", "warning");
        }
    })
}
*/