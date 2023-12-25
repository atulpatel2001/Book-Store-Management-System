//search Book Category
/*const searchBookCategory=async()=>{
      let query=$("#search-book-category").val();
    if (query == '') {
            $(".search-result-book-category").hide();
        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-book-category/"+query;
            fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                      console.log(data);
                                        let tableHtml = '';
                                        data.forEach(category => {
                                            let table =  bookCategoryTable(category);
                                            tableHtml += table;
                                        });
                                        $(".search-result-book-category").html(tableHtml);
                                        $(".search-result-book-category").show();
                     });

        }
}
*/

const searchBookCategory = async () => {
    let query = $("#search-book-category").val();

    if (query == '') {
        $(".search-result-book-category").hide();
    } else {
        const url = "/Book-Store/search/search-book-category/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let tableHtml = '';
                data.forEach(category => {
                    let table = bookCategoryTable(category);
                    tableHtml += table;
                });
                $(".search-result-book-category").html(tableHtml);
                $(".search-result-book-category").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};



function bookCategoryTable(category) {
    let table = '<div class="table-responsive m-5">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#BOOK-CATEGORY ' + category.categoryId + '</td>' +
        '<td><h5 class="font-medium mb-0">' + category.categoryTitle + '</h5></td>' +
        '<td><span class="text-muted">' + category.categoryAddDate + '</span><br></td>';

    table += '</tr></tbody></table></div>';
    return table;
}


//search Book
/*
const searchBook=()=>{
      let query=$("#search-book").val();
    if (query == '') {
            $(".search-result-book").hide();
            $("#all-book").show();
        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-book/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                        let cardHtml = '';
                                          data.forEach(book => {
                                            let card =  bookCard(book);
                                            cardHtml += card;
                                        });
                                        $(".search-result-book").html(cardHtml);
                                        $(".search-result-book").show();
                                        $("#all-book").hide();
                     });

        }
}

*/

const searchBook = () => {
    let query = $("#search-book").val();

    if (query == '') {
        $(".search-result-book").hide();
        $("#all-book").show();
    } else {
        const url = "/Book-Store/search/search-book/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                let cardHtml = '';
                data.forEach(book => {
                    let card = bookCard(book);
                    cardHtml += card;
                });
                $(".search-result-book").html(cardHtml);
                $(".search-result-book").show();
                $("#all-book").hide();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};


function bookCard(book) {
     var card ="<div class='col-md-4 mb-2'>" +
                "<div class='card' style='width: 12rem;'>" +
                    "<img src=" + book.bookImageUrl + " class='card-img-top' alt='...' style='height:200px;'>" +
                    "<div class='card-body'>" +
                        "<div class='text-center'>" +
                            "<h5 class='card-title'>" + book.bookTitle + "</h5>" +
                            "<p class='card-text'>" + book.bookAuthor + "</p>" +
                            "<a type='button' class='btn btn-outline-info btn-circle btn-sm btn-circle' href='/Book-Store/admin/"+book.bookId+"/book-Info'><i class='fa fa-info-circle'></i> </a>" +
                            "<button type='button' class='btn btn-outline-info btn-circle btn-sm btn-circle ml-2' data-bs-toggle='modal' data-bs-target='#exampleModal-" + book.bookId + "'><i class='fa fa-edit'></i></button>" +
                        "</div>" +
                    "</div>" +
                "</div>" +
            "</div>";
    return card;
}


//search Manager
/*
const searchManager=()=>{
      let query=$("#search-manager").val();
    if (query == '') {
            $(".search-result-manager").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-manager/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let tableHtml = '';
                                          data.forEach(manager => {

                                            let table =  Manager(manager);
                                            tableHtml += table;
                                        });
                                        $(".search-result-manager").html(tableHtml);
                                        $(".search-result-manager").show();

                     });

        }
}
*/

const searchManager = () => {
    let query = $("#search-manager").val();

    if (query == '') {
        $(".search-result-manager").hide();
    } else {
        const url = "/Book-Store/search/search-manager/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let tableHtml = '';
                data.forEach(manager => {
                    let table = Manager(manager);
                    tableHtml += table;
                });
                $(".search-result-manager").html(tableHtml);
                $(".search-result-manager").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};


function Manager(manager) {
     var table ='<div class="table-responsive">'+
                					'<table class="table table-hover table-nowrap"><tbody>';
            table+='<tr><td><a class="text-heading font-semibold" style="font-size:16px;font-width:30px">'+manager.employeeName +'</a></td>'+
                           '<td style="font-size:16px;font-width:30px">'+manager.employeeEmail +'</td>'+
                           '<td><span class="text-heading" href="# style="font-size:16px;font-width:30px">'+manager.phoneNumber +'</span></td>'+
                           '<td style="font-size:16px;font-width:30px">'+manager.pincode +'</td>'+
                          '<td><span class="fa fa-inr" ><i style="font-size:16px;font-width:30px">'+manager.salary +'</i></span></td>'+
                           '<td class="text-end"><a  class="btn btn-sm btn-neutral" href="/Book-Store/admin/'+manager.empId+'/manager-info">View</a></td></tr>';

                       table+='</tbody></table></div>';
    return table;
}

//search Staff
/*
const searchStaff=()=>{
      let query=$("#search-staff").val();
    if (query == '') {
            $(".search-result-staff").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-staff/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let tableHtml = '';
                                          data.forEach(staff => {

                                            let table =  Staff(staff);
                                            tableHtml += table;
                                        });
                                        $(".search-result-staff").html(tableHtml);
                                        $(".search-result-staff").show();

                     });

        }
}
*/

const searchStaff = () => {
    let query = $("#search-staff").val();

    if (query == '') {
        $(".search-result-staff").hide();
    } else {
        const url = "/Book-Store/search/search-staff/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let tableHtml = '';
                data.forEach(staff => {
                    let table = Staff(staff);
                    tableHtml += table;
                });
                $(".search-result-staff").html(tableHtml);
                $(".search-result-staff").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};

function Staff(staff) {
     var table ='<div class="table-responsive">'+
                					'<table class="table table-hover table-nowrap"><tbody>';
            table+='<tr><td><a class="text-heading font-semibold" style="font-size:16px;font-width:30px">'+staff.employeeName +'</a></td>'+
                           '<td style="font-size:16px;font-width:30px">'+staff.employeeEmail +'</td>'+
                           '<td><span class="text-heading" href="# style="font-size:16px;font-width:30px">'+staff.phoneNumber +'</span></td>'+
                           '<td style="font-size:16px;font-width:30px">'+staff.pincode +'</td>'+
                          '<td><span class="fa fa-inr" ><i style="font-size:16px;font-width:30px">'+staff.salary +'</i></span></td>'+
                           '<td class="text-end"><a  class="btn btn-sm btn-neutral" href="/Book-Store/admin/'+staff.empId+'/manager-info">View</a></td></tr>';

                       table+='</tbody></table></div>';
    return table;
}



//search customer
/*
const searchCustomer=()=>{
      let query=$("#search-cus").val();
    if (query == '') {
            $(".search-result-cus").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-customer/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let tableHtml = '';
                                          data.forEach(customer => {

                                            let table =  Customer(customer);
                                            tableHtml += table;
                                        });
                                        $(".search-result-cus").html(tableHtml);
                                        $(".search-result-cus").show();

                     });

        }
}
*/
const searchCustomer = () => {
    let query = $("#search-cus").val();

    if (query == '') {
        $(".search-result-cus").hide();
    } else {
        const url = "/Book-Store/search/search-customer/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let tableHtml = '';
                data.forEach(customer => {
                    let table = Customer(customer);
                    tableHtml += table;
                });
                $(".search-result-cus").html(tableHtml);
                $(".search-result-cus").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};


function Customer(customer) {
     var table ='<div class="table-responsive">'+
                					'<table class="table table-hover table-nowrap"><tbody>';
            table+='<tr><td><a class="text-heading font-semibold" style="font-size:16px;font-width:30px">'+customer.customerName +'</a></td>'+
                           '<td style="font-size:16px;font-width:30px">'+customer.customerEmailId +'</td>'+
                           '<td><span class="text-heading" href="# style="font-size:16px;font-width:30px">'+customer.customerRole +'</span></td>'+
                           '<td style="font-size:16px;font-width:30px">'+customer.customerJoinDate +'</td>'+
                           '<td class="text-end"><a  class="btn btn-sm btn-neutral" href="/Book-Store/admin/'+customer.customerId+'/delete-customer"><i class="bi bi-trash"></i></a></td></tr>';

                       table+='</tbody></table></div>';
    return table;
}



//search Staff in manager page
/*
const searchStaffm=()=>{
      let query=$("#search-staffm").val();
    if (query == '') {
            $(".search-result-staffm").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-staffm/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let tableHtml = '';
                                          data.forEach(staff => {

                                            let table =  Staffm(staff);
                                            tableHtml += table;
                                        });
                                        $(".search-result-staffm").html(tableHtml);
                                        $(".search-result-staffm").show();

                     });

        }
}
*/

const searchStaffm = () => {
    let query = $("#search-staffm").val();

    if (query == '') {
        $(".search-result-staffm").hide();
    } else {
        const url = "/Book-Store/search/search-staffm/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let tableHtml = '';
                data.forEach(staff => {
                    let table = Staffm(staff);
                    tableHtml += table;
                });
                $(".search-result-staffm").html(tableHtml);
                $(".search-result-staffm").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};


function Staffm(staff) {
     var table ='<div class="table-responsive">'+
                					'<table class="table table-hover table-nowrap"><tbody>';
            table+='<tr><td><a class="text-heading font-semibold" style="font-size:16px;font-width:30px">'+staff.employeeName +'</a></td>'+
                           '<td style="font-size:16px;font-width:30px">'+staff.employeeEmail +'</td>'+
                           '<td><span class="text-heading" href="# style="font-size:16px;font-width:30px">'+staff.phoneNumber +'</span></td>'+
                           '<td style="font-size:16px;font-width:30px">'+staff.pincode +'</td>'+
                          '<td><span class="fa fa-inr" ><i style="font-size:16px;font-width:30px">'+staff.salary +'</i></span></td>'+
                           '<td class="text-end"><a  class="btn btn-sm btn-neutral" href="/Book-Store/manager/'+staff.empId+'/staff-info">View</a></td></tr>';

                       table+='</tbody></table></div>';
    return table;
}



/*
const searchPendingOrder=()=>{
      let query=$("#search-delivered-order").val();
    if (query == '') {
            $(".search-result-pending-order").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-pending-order/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let orderHtml = '';
                                          data.forEach(order => {

                                            let orderData =  PendingOrder(order);
                                            orderHtml += orderData;
                                        });
                                        $(".search-result-pending-order").html(orderHtml);
                                        $(".search-result-pending-order").show();

                     });

        }
}
*/

const searchPendingOrder = () => {
    let query = $("#search-delivered-order").val();

    if (query == '') {
        $(".search-result-pending-order").hide();
    } else {
        const url = "/Book-Store/search/search-pending-order/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let orderHtml = '';
                data.forEach(order => {
                    let orderData = PendingOrder(order);
                    orderHtml += orderData;
                });
                $(".search-result-pending-order").html(orderHtml);
                $(".search-result-pending-order").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};

function PendingOrder(order) {
     var htmlCode ='<div class="card shadow-0 border mb-4 " style="margin-right:50px;margin-left:25px;font-size:19px;">'+
                '<div class="card-body"><div class="row"><div class="col-md-2">'+
                '<a href="/Book-Store/admin/'+order.orderId+'/order-detail">'+
               ' <img src="'+order.bookImage+'" style="height:80px;width:80px;" class="img-fluid" alt="Phone"></a></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
               '<p class="text-muted mb-0">'+order.bookTitle+'</p></div><div class="col-md-2 text-center d-flex align-items-center">'+
               '<p class="text-muted mb-0 small" >'+order.orderId+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
                '<p class="text-muted mb-0 small">'+order.bookAuthor+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
                '<p class="text-muted mb-0 small" order.bookQuantity}" >Qty: '+order.bookQuantity+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
               ' <p class="text-muted mb-0 small">$'+order.payAmount+'</p></div></div></div></div>';
    return htmlCode;
}



/*
const searchHistoryOrder=()=>{
      let query=$("#search-history-order").val();
    if (query == '') {
            $(".search-result-history-order").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/search-delivered-order/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let orderHtml = '';
                                          data.forEach(order => {

                                            let orderData =  historyOrder(order);
                                            orderHtml += orderData;
                                        });
                                        $(".search-result-history-order").html(orderHtml);
                                        $(".search-result-history-order").show();

                     });

        }
}
*/


const searchHistoryOrder = () => {
    let query = $("#search-history-order").val();

    if (query == '') {
        $(".search-result-history-order").hide();
    } else {
        const url = "/Book-Store/search/search-delivered-order/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let orderHtml = '';
                data.forEach(order => {
                    let orderData = historyOrder(order);
                    orderHtml += orderData;
                });
                $(".search-result-history-order").html(orderHtml);
                $(".search-result-history-order").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
};


function historyOrder(order) {
     var htmlCode ='<div class="card shadow-0 border mb-4 " style="margin-right:50px;margin-left:25px;font-size:19px;">'+
                '<div class="card-body"><div class="row"><div class="col-md-2">'+
                '<a href="/Book-Store/admin/'+order.orderId+'/order-detail">'+
               ' <img src="'+order.bookImage+'" style="height:80px;width:80px;" class="img-fluid" alt="Phone"></a></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
               '<p class="text-muted mb-0">'+order.bookTitle+'</p></div><div class="col-md-2 text-center d-flex align-items-center">'+
               '<p class="text-muted mb-0 small" >'+order.orderId+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
                '<p class="text-muted mb-0 small">'+order.bookAuthor+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
                '<p class="text-muted mb-0 small" order.bookQuantity}" >Qty: '+order.bookQuantity+'</p></div><div class="col-md-2 text-center d-flex justify-content-center align-items-center">'+
               ' <p class="text-muted mb-0 small">$'+order.payAmount+'</p></div></div></div></div>';
    return htmlCode;
}




/*
const searchTransaction=()=>{
      let query=$("#search-transaction").val();
      console.log(query)
    if (query == '') {
            $(".search-result-transaction").hide();

        }
        else{
             const url="http://localhost:9090/Book-Store/search/transaction/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
              console.log(data);
                                        let htmlData = '';
                                          data.forEach(transaction => {

                                            let data2 =  TransactionData(transaction);
                                            htmlData += data2;
                                        });
                                        $(".search-result-transaction").html(htmlData);
                                        $(".search-result-transaction").show();

                     });

        }
}
*/


const searchTransaction = () => {
    let query = $("#search-transaction").val();
    console.log(query);

    if (query == '') {
        $(".search-result-transaction").hide();
    } else {
        const url = "/Book-Store/search/transaction/" + query;

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (data) {
                console.log(data);
                let htmlData = '';
                data.forEach(transaction => {
                    let data2 = TransactionData(transaction);
                    htmlData += data2;
                });
                $(".search-result-transaction").html(htmlData);
                $(".search-result-transaction").show();
            },
            error: function (error) {
                console.error("Error fetching data: ", error);
            }
        });
    }
}

function TransactionData(transaction) {
     var table ='<div class="table-responsive">'+
                '<table class="table table-hover table-nowrap"><tbody>';
                 table+='<tr><td><span class="badge badge-lg badge-dot">'+
                  '<i class="bg-success"></i><a style="text-decoration-none;color:black;" href="/Book-Store/admin/'+transaction.orderId+'/order-detail"><span style="text-decoration-none;color:black;">'+transaction.orderId+'</span></a>'+
                 '</span></td><td><a th:href="/Book-Store/admin/'+transaction.bookId+'/book-Info">'+
                 '<img style="height:50px;" src="'+transaction.bookImage+'" alt="book title">'+
                  '<span style="font-size:13px;text-decoration-none;color:black;">'+transaction.bookTitle+'</span>'+
                   '</a></td><td th:text="${transaction.amount}">$'+transaction.amount+'</td>'+
                  '<td>'+transaction.transcationStatus+'</td>'+
                   '<td>'+transaction.paymentId+'</td></tr>';
                   table+='</tbody></table></div>';
    return table;
}
