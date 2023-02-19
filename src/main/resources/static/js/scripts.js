$(document).ready(function (){
    $('.table .delBtn').on('click', function (event){
        event.preventDefault();
        let href = $(this).attr('href');
        $('#myModal #delRef').attr('href', href);
        $('#myModal').modal();
    });
});