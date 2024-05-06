from datetime import datetime

from django.contrib import auth
from django.contrib.auth.decorators import login_required
from django.core.files.storage import FileSystemStorage
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect
from SM_Street_App.models import *
import json
from django.http import JsonResponse

# Create your views here.


def login(request):
    return render(request, "login_index.html")


def logout(request):
    auth.logout(request)
    return render(request, "login_index.html")


def login_action(request):
    username = request.POST['Username']
    password = request.POST['Password']
    try:
        login_obj = Login.objects.get(Username=username, Password=password)
        if login_obj.Type == "admin":
            auth_obj = auth.authenticate(username='admin', password='admin')
            if auth_obj is not None:
                auth.login(request, auth_obj)
            return HttpResponse('''<script>alert("welcome to admin home"); window.location="/admin_home"</script>''')

        elif login_obj.Type == "rental":
            auth_obj = auth.authenticate(username='admin', password='admin')
            if auth_obj is not None:
                auth.login(request, auth_obj)
            request.session['rental_lid'] = login_obj.id
            return HttpResponse('''<script>alert("welcome to rental home"); window.location="/rental_home"</script>''')

        elif login_obj.Type == "shop":
            auth_obj = auth.authenticate(username='admin', password='admin')
            if auth_obj is not None:
                auth.login(request, auth_obj)
            request.session['shop_lid'] = login_obj.id
            return HttpResponse('''<script>alert("welcome to shop home"); window.location="/shop_home"</script>''')

        elif login_obj.Type == "seller":
            request.session['seller_lid'] = login_obj.id
            return HttpResponse('''<script>alert("welcome to seller home"); window.location="/seller_home"</script>''')
    except:
        return HttpResponse('''<script>alert("invalid username and password!"); window.location="/" </script>''')


# ///////////////////// Admin ///////////////////////

@login_required(login_url='/')
def admin_home(request):
    return render(request, "admin/admin_home.html")


@login_required(login_url='/')
def shop_verify(request):
    shop_obj = Shop.objects.all()
    return render(request, "admin/shop_verify.html", {"shop_obj": shop_obj})


@login_required(login_url='/')
def shop_verify_search(request):
    shop_name = request.POST['shop_name']
    shop_obj = Shop.objects.filter(ShopName__istartswith=shop_name)
    return render(request, "admin/shop_verify.html", {"shop_obj": shop_obj, 'shop_name': shop_name})


@login_required(login_url='/')
def shop_accept(request, shop_id):
    shop_obj = Shop.objects.get(id=shop_id)
    login_obj = Login.objects.get(id=shop_obj.LOGIN_id)
    login_obj.Type = "shop"
    login_obj.save()
    return HttpResponse('''<script>alert("shop accepted successfully"); window.location="/shop_verify#about"</script>''')


@login_required(login_url='/')
def shop_reject(request, shop_id):
    shop_obj = Shop.objects.get(id=shop_id)
    login_obj = Login.objects.get(id=shop_obj.LOGIN_id)
    login_obj.Type = "reject"
    login_obj.save()
    return HttpResponse('''<script>alert("shop rejected successfully"); window.location="/shop_verify#about"</script>''')


@login_required(login_url='/')
def view_category(request):
    category_obj = Category.objects.all()
    return render(request, "admin/view_category.html", {'category_obj': category_obj})


@login_required(login_url='/')
def add_category(request):
    return render(request, "admin/add_category.html")


@login_required(login_url='/')
def add_category_action(request):
    category = request.POST['Category']
    category_obj = Category()
    category_obj.CategoryName = category
    category_obj.save()
    return HttpResponse('''<script>alert("Category added successfully"); window.location="/view_category#about"</script>''')


@login_required(login_url='/')
def delete_category(request, cat_id):
    category_obj = Category.objects.get(id=cat_id)
    category_obj.delete()
    return HttpResponse('''<script>alert("Category deleted successfully"); window.location="/view_category#about"</script>''')


@login_required(login_url='/')
def view_shop_rating(request):
    shop_obj = Shop.objects.all()
    return render(request, "admin/view_shop_rating.html", {'shop_obj': shop_obj})


@login_required(login_url='/')
def search_shop_action(request):
    shop_id = request.POST['selected_shop']
    rating_obj = Rating.objects.filter(SHOP_id=shop_id)
    return render(request, "admin/shop_rating.html", {'rating_obj': rating_obj})


@login_required(login_url='/')
def view_complaint_type(request):
    return render(request, "admin/view_complaint_type.html")


@login_required(login_url='/')
def view_complaint_type_action(request):
    complaint_type = request.POST['select_type']
    request.session['complaint_type'] = complaint_type
    if complaint_type == 'user':
        complaint_obj = Complaint.objects.all()
        return render(request, "admin/view_user_complaint.html", {'complaint_obj': complaint_obj})
    elif complaint_type == 'shop':
        complaint_obj = ShopComplaint.objects.all()
        return render(request, "admin/view_shop_complaint.html", {'complaint_obj': complaint_obj})


@login_required(login_url='/')
def view_complaint_search(request):
    date = request.POST['Date_search']
    complaint_obj = Complaint.objects.filter(Date=date)
    return render(request, "admin/view_user_complaint.html", {'complaint_obj': complaint_obj, 'search': date})


@login_required(login_url='/')
def view_shop_complaint_search(request):
    date = request.POST['Date_search']
    complaint_obj = ShopComplaint.objects.filter(Date=date)
    return render(request, "admin/view_shop_complaint.html", {'complaint_obj': complaint_obj, 'search': date})


@login_required(login_url='/')
def complaint_reply(request, complaint_id):
    request.session['complaint_id'] = complaint_id
    return render(request, "admin/complaint_reply.html")


@login_required(login_url='/')
def complaint_reply_action(request):
    reply = request.POST['Reply']
    complaint_obj = Complaint.objects.get(id=request.session['complaint_id'])
    complaint_obj.Reply = reply
    complaint_obj.save()
    return HttpResponse('''<script>alert("Reply sent successfully"); window.location="/admin_home#about"</script>''')


@login_required(login_url='/')
def shop_complaint_reply(request, complaint_id):
    request.session['complaint_id'] = complaint_id
    return render(request, "admin/shop_complaint_reply.html")


@login_required(login_url='/')
def shop_complaint_reply_action(request):
    reply = request.POST['Reply']
    complaint_obj = ShopComplaint.objects.get(id=request.session['complaint_id'])
    complaint_obj.Reply = reply
    complaint_obj.save()
    return HttpResponse('''<script>alert("Reply sent successfully"); window.location="/admin_home#about"</script>''')


# def select_shop_notification(request):
#     shop_obj = Shop.objects.all()
#     return render(request, "admin/select_shop_notification.html", {'shop_obj': shop_obj})


@login_required(login_url='/')
def send_notification(request):
    # request.session['shop_id'] = request.POST['selected_shop']
    return render(request, "admin/send_notification.html")


@login_required(login_url='/')
def send_notification_action(request):
    notification = request.POST['Notification']
    notification_obj = Notification()
    notification_obj.Notifications = notification
    notification_obj.Date = datetime.now()
    notification_obj.save()
    return HttpResponse('''<script>alert("Notification sent successfully"); 
    window.location="/admin_home#about"</script>''')


@login_required(login_url='/')
def approve_sellers(request):
    seller_obj = Seller.objects.all()
    return render(request, "admin/approve_sellers.html", {'seller_obj': seller_obj})


@login_required(login_url='/')
def accept_seller(request, seller_id):
    seller_obj = Seller.objects.get(id=seller_id)
    login_obj = Login.objects.get(id=seller_obj.LOGIN.id)
    login_obj.Type = "seller"
    login_obj.save()
    return HttpResponse('''<script>alert("Accepted"); window.location="/approve_sellers#about"</script>''')


@login_required(login_url='/')
def reject_seller(request, seller_id):
    seller_obj = Seller.objects.get(id=seller_id)
    login_obj = Login.objects.get(id=seller_obj.LOGIN.id)
    login_obj.Type = "rejected"
    login_obj.save()
    return HttpResponse('''<script>alert("Rejected"); window.location="/approve_sellers#about"</script>''')


@login_required(login_url='/')
def approve_sellers_search(request):
    seller = request.POST['seller_search']
    seller_obj = Seller.objects.filter(FirstName__istartswith=seller)
    return render(request, "admin/approve_sellers.html", {'seller_obj': seller_obj, 'seller': seller})


#  ////////////////////////// RENTAL //////////////////////////////////////////


@login_required(login_url='/')
def rental_register(request):
    return render(request, "rental_registration_index.html")


@login_required(login_url='/')
def rental_register_action(request):
    name = request.POST['Name']
    place = request.POST['Place']
    post = request.POST['Post']
    pin = request.POST['Pin']
    phone = request.POST['Phone']
    email = request.POST['Email']
    latitude = request.POST['Latitude']
    longitude = request.POST['Longitude']
    username = request.POST['Username']
    password = request.POST['Password']

    login_obj = Login()
    login_obj.Username = username
    login_obj.Password = password
    login_obj.Type = "rental"
    login_obj.save()

    rental_obj = Rental()
    rental_obj.Name = name
    rental_obj.Place = place
    rental_obj.Post = post
    rental_obj.Pin = pin
    rental_obj.Phone = phone
    rental_obj.Email = email
    rental_obj.Latitude = latitude
    rental_obj.Longitude = longitude
    rental_obj.LOGIN = login_obj
    rental_obj.save()
    return HttpResponse('''<script>alert("successfully registered"); window.location="/"</script>''')


@login_required(login_url='/')
def rental_home(request):
    return render(request, "Rental/rental_home_index.html")


@login_required(login_url='/')
def view_booking(request):
    ob = RentalBookingDetails.objects.filter(PRODUCT__RENTAL__LOGIN_id=request.session['rental_lid'])
    d = []
    for i in ob:
        d.append(i.RENTAL_BOOKING.id)
    obb = RentalBooking.objects.filter(id__in=d)
    return render(request, "Rental/view_rental_booking.html", {'booking_obj': obb})


@login_required(login_url='/')
def view_more_booking(request, booking_id):
    request.session['rental_booking_id'] = booking_id
    booking_obj = RentalBookingDetails.objects.filter(RENTAL_BOOKING=booking_id)
    return render(request, "Rental/rental_booking_update.html", {'booking_obj': booking_obj})


@login_required(login_url='/')
def accept_rental_request(request):
    booking_obj = RentalBooking.objects.get(id=request.session['rental_booking_id'])
    booking_obj.Status = "accepted"
    booking_obj.save()
    return HttpResponse('''<script>alert("Requested accepted"); window.location="/view_booking#about"</script>''')


@login_required(login_url='/')
def reject_rental_request(request):
    booking_obj = RentalBooking.objects.get(id=request.session['rental_booking_id'])
    booking_obj.Status = "rejected"
    booking_obj.save()
    return HttpResponse('''<script>alert("Requested rejected"); window.location="/view_booking#about"</script>''')


@login_required(login_url='/')
def view_booking_search(request):
    search_date = request.POST['Search_Date']
    booking_obj = RentalBookingDetails.objects.filter(RENTAL_BOOKING__RENTAL__LOGIN_id=request.session['rental_lid'],
                                                      RENTAL_BOOKING__Date=search_date)
    return render(request, "Rental/view_booking.html", {'booking_obj': booking_obj, 'search': search_date})


@login_required(login_url='/')
def manage_product(request):
    product_obj = RentalProduct.objects.filter(RENTAL__LOGIN_id=request.session['rental_lid'])
    return render(request, "Rental/manage_product.html", {'product_obj': product_obj})


@login_required(login_url='/')
def manage_product_search(request):
    search = request.POST['search']
    product_obj = RentalProduct.objects.filter(RENTAL__LOGIN_id=request.session['rental_lid'],
                                               ProductName__istartswith=search)
    return render(request, "Rental/manage_product.html", {'product_obj': product_obj, 'search': search})


@login_required(login_url='/')
def add_rental_product(request):
    category_obj = Category.objects.all()
    return render(request, "Rental/add_rental_product.html", {'category_obj': category_obj})


@login_required(login_url='/')
def add_rental_product_action(request):
    product_name = request.POST['Product_name']
    details = request.POST['Details']
    stock = request.POST['stock']
    price = request.POST['Price']
    selected_category = request.POST['selected_category']
    image = request.FILES['Image']
    fss = FileSystemStorage()
    product_image = fss.save(image.name, image)
    product_obj = RentalProduct()
    product_obj.ProductName = product_name
    product_obj.ProductDetails = details
    product_obj.Price = price
    product_obj.StockNo = stock
    product_obj.CATEGORY = Category.objects.get(id=selected_category)
    product_obj.Image = product_image
    product_obj.RENTAL = Rental.objects.get(LOGIN_id=request.session['rental_lid'])
    product_obj.save()
    return HttpResponse('''<script>alert("Product added"); window.location="manage_product#about"</script>''')


@login_required(login_url='/')
def update_rental_product(request, product_id):
    request.session['product_id'] = product_id
    category_obj = Category.objects.all()
    product_obj = RentalProduct.objects.get(id=product_id)
    return render(request, "Rental/update_rental_product.html", {'category_obj': category_obj, 'product_obj': product_obj})


@login_required(login_url='/')
def update_rental_product_action(request):
    product_name = request.POST['Product_name']
    details = request.POST['Details']
    price = request.POST['Price']
    stock = request.POST['stock']
    selected_category = request.POST['selected_category']
    if "Image" in request.FILES:
        image = request.FILES['Image']
        fss = FileSystemStorage()
        product_image = fss.save(image.name, image)
        product_obj = RentalProduct.objects.get(id=request.session['product_id'])
        product_obj.ProductName = product_name
        product_obj.ProductDetails = details
        product_obj.Price = price
        product_obj.StockNo = stock
        product_obj.CATEGORY = Category.objects.get(id=selected_category)
        product_obj.Image = product_image
        product_obj.save()
        return HttpResponse('''<script>alert("Product updated"); window.location="/manage_product#about"</script>''')
    else:
        product_obj = RentalProduct.objects.get(id=request.session['product_id'])
        product_obj.ProductName = product_name
        product_obj.ProductDetails = details
        product_obj.Price = price
        product_obj.StockNo = stock
        product_obj.CATEGORY = Category.objects.get(id=selected_category)
        product_obj.save()
        return HttpResponse('''<script>alert("Product updated"); window.location="/manage_product#about"</script>''')


@login_required(login_url='/')
def delete_rental_product(request, product_id):
    product_obj = RentalProduct.objects.get(id=product_id)
    product_obj.delete()
    return HttpResponse('''<script>alert("Product deleted"); window.location="/manage_product#about"</script>''')


# ///////////////////////////////////////////////  SHOP  ///////////////////////////////////////////////////////


@login_required(login_url='/')
def shop_register(request):
    return render(request, "shop_registration_index.html")


@login_required(login_url='/')
def shop_register_action(request):
    shop_name = request.POST['Shop_name']
    place = request.POST['Place']
    post = request.POST['Post']
    pin = request.POST['Pin']
    phone = request.POST['Phone']
    email = request.POST['Email']
    latitude = request.POST['Latitude']
    longitude = request.POST['Longitude']
    username = request.POST['Username']
    password = request.POST['Password']

    login_obj = Login()
    login_obj.Username = username
    login_obj.Password = password
    login_obj.Type = "pending"
    login_obj.save()

    shop_obj = Shop()
    shop_obj.ShopName = shop_name
    shop_obj.Place = place
    shop_obj.Post = post
    shop_obj.Pin = pin
    shop_obj.Phone = phone
    shop_obj.Email = email
    shop_obj.Latitude = latitude
    shop_obj.Longitude = longitude
    shop_obj.LOGIN = login_obj
    shop_obj.save()
    return HttpResponse('''<script>alert("Shop registered"); window.location="/"</script>''')


@login_required(login_url='/')
def shop_home(request):
    return render(request, "shop/shop_home.html")


@login_required(login_url='/')
def manage_shop_product(request):
    cat_obj = Category.objects.all()
    product_obj = Product.objects.filter(LOGIN__id=request.session['shop_lid'])
    return render(request, "shop/manage_shop_product.html", {'product_obj': product_obj, 'cat_obj': cat_obj})


@login_required(login_url='/')
def manage_shop_product_search(request):
    cat_obj = Category.objects.all()
    category = request.POST['Category_search']
    product_obj = Product.objects.filter(LOGIN__id=request.session['shop_lid'], CATEGORY_id=category)
    return render(request, "shop/manage_shop_product.html", {'product_obj': product_obj, 'cat_obj': cat_obj})


@login_required(login_url='/')
def add_shop_product(request):
    category_obj = Category.objects.all()
    return render(request, "shop/add_shop_product.html", {'category_obj': category_obj})


@login_required(login_url='/')
def add_shop_product_action(request):
    product_name = request.POST['Product_name']
    details = request.POST['Details']
    price = request.POST['Price']
    selected_category = request.POST['selected_category']
    image = request.FILES['Image']
    fss = FileSystemStorage()
    product_image = fss.save(image.name, image)
    product_obj = Product()
    product_obj.ProductName = product_name
    product_obj.ProductDetails = details
    product_obj.Price = price
    product_obj.CATEGORY = Category.objects.get(id=selected_category)
    product_obj.Image = product_image
    product_obj.LOGIN = Login.objects.get(id=request.session['shop_lid'])
    product_obj.save()
    return HttpResponse('''<script>alert("Product added"); window.location="manage_shop_product#about"</script>''')


@login_required(login_url='/')
def update_shop_product(request, product_id):
    category_obj = Category.objects.all()
    product_obj = Product.objects.get(id=product_id)
    request.session['product_id'] = product_id
    return render(request, "shop/update_shop_product.html", {'category_obj': category_obj, 'product_obj': product_obj})


@login_required(login_url='/')
def update_shop_product_action(request):
    product_name = request.POST['Product_name']
    details = request.POST['Details']
    price = request.POST['Price']
    selected_category = request.POST['selected_category']
    if 'Image' in request.FILES:
        image = request.FILES['Image']
        fss = FileSystemStorage()
        product_image = fss.save(image.name, image)
        product_obj = Product.objects.get(id=request.session['product_id'])
        product_obj.ProductName = product_name
        product_obj.ProductDetails = details
        product_obj.Price = price
        product_obj.CATEGORY = Category.objects.get(id=selected_category)
        product_obj.Image = product_image
        product_obj.LOGIN = Login.objects.get(id=request.session['shop_lid'])
        product_obj.save()
        return HttpResponse('''<script>alert("Product updated"); window.location="/manage_shop_product#about"</script>''')
    else:
        product_obj = Product.objects.get(id=request.session['product_id'])
        product_obj.ProductName = product_name
        product_obj.ProductDetails = details
        product_obj.Price = price
        product_obj.CATEGORY = Category.objects.get(id=selected_category)
        product_obj.LOGIN = Login.objects.get(id=request.session['shop_lid'])
        product_obj.save()
        return HttpResponse('''<script>alert("Product updated"); window.location="/manage_shop_product#about"</script>''')


@login_required(login_url='/')
def delete_shop_product(request, product_id):
    product_obj = Product.objects.get(id=product_id)
    product_obj.delete()
    return HttpResponse('''<script>alert("Product Deleted"); window.location="/manage_shop_product#about"</script>''')


@login_required(login_url='/')
def manage_offers(request):
    offers = Offer.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/manage_offers.html", {'offers': offers})


@login_required(login_url='/')
def manage_offers_search(request):
    search = request.POST['textfield']
    offers = Offer.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'], PRODUCT__ProductName__istartswith=search)
    return render(request, "shop/manage_offers.html", {'offers': offers, 'search': search})


@login_required(login_url='/')
def add_offers(request):
    cur_date = datetime.now().strftime("%Y-%m-%d")
    product_obj = Product.objects.filter(LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/add_offer.html", {'product_obj': product_obj, 'cur_date': cur_date})


@login_required(login_url='/')
def add_offer_action(request):
    product_id = request.POST['select']
    start_date = request.POST['Start_date']
    end_date = request.POST['End_date']
    details = request.POST['Details']
    offer_obj = Offer()
    offer_obj.StartDate = start_date
    offer_obj.EndDate = end_date
    offer_obj.Details = details
    offer_obj.PRODUCT = Product.objects.get(id=product_id)
    offer_obj.save()
    return HttpResponse('''<script>alert("Offer added"); window.location="/manage_offers#about"</script>''')


@login_required(login_url='/')
def update_offers(request, offer_id):
    cur_date = datetime.now().strftime("%Y-%m-%d")
    request.session['offer_id'] = offer_id
    product_obj = Product.objects.all()
    offer_obj = Offer.objects.get(id=offer_id)
    return render(request, "shop/update_offer.html", {'product_obj': product_obj, 'stare_date': offer_obj.StartDate,
                                                      'end_date': offer_obj.EndDate, 'offer_obj': offer_obj,
                                                      'cur_date': cur_date})


@login_required(login_url='/')
def update_offer_action(request):
    product_id = request.POST['select']
    start_date = request.POST['Start_date']
    end_date = request.POST['End_date']
    details = request.POST['Details']
    offer_obj = Offer.objects.get(id=request.session['offer_id'])
    offer_obj.StartDate = start_date
    offer_obj.EndDate = end_date
    offer_obj.Details = details
    offer_obj.PRODUCT = Product.objects.get(id=product_id)
    offer_obj.save()
    return HttpResponse('''<script>alert("Offer updated"); window.location="/manage_offers#about"</script>''')


@login_required(login_url='/')
def delete_offers(request, offer_id):
    offer_obj = Offer.objects.get(id=offer_id)
    offer_obj.delete()
    return HttpResponse('''<script>alert("Offer deleted"); window.location="/manage_offers#about"</script>''')


@login_required(login_url='/')
def view_notifications_shop(request):
    notification_obj = Notification.objects.all()
    return render(request, "shop/view_notifications.html", {'notification_obj': notification_obj})


@login_required(login_url='/')
def view_rating_review(request):
    rating_obj = Rating.objects.filter(SHOP__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/view_rating_review.html", {'rating_obj': rating_obj})


@login_required(login_url='/')
def view_rating_review_search(request):
    search_date = request.POST['Search_Date']
    rating_obj = Rating.objects.filter(SHOP__LOGIN_id=request.session['shop_lid'], Date=search_date)
    return render(request, "shop/view_rating_review.html", {'rating_obj': rating_obj, 'search_date': search_date})


@login_required(login_url='/')
def manage_special_offers(request):
    offers = SpecialOffers.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/manage_special_offers.html", {'offers': offers})


@login_required(login_url='/')
def manage_special_offers_search(request):
    search = request.POST['search']
    offers = SpecialOffers.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'],
                                          PRODUCT__ProductName__istartswith=search)
    return render(request, "shop/manage_special_offers.html", {'offers': offers, 'search': search})


@login_required(login_url='/')
def add_special_offers(request):
    cur_date = datetime.now().strftime("%Y-%m-%d")
    product_obj = Product.objects.filter(LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/add_special_offer.html", {'product_obj': product_obj, 'cur_date': cur_date})


@login_required(login_url='/')
def add_special_offer_action(request):
    product_id = request.POST['select']
    start_date = request.POST['Start_date']
    end_date = request.POST['End_date']
    details = request.POST['Details']
    offer_obj = SpecialOffers()
    offer_obj.StartDate = start_date
    offer_obj.EndDate = end_date
    offer_obj.Details = details
    offer_obj.PRODUCT = Product.objects.get(id=product_id)
    offer_obj.save()
    return HttpResponse('''<script>alert("Offer added"); window.location="/manage_special_offers#about"</script>''')


@login_required(login_url='/')
def update_special_offers(request, offer_id):
    cur_date = datetime.now().strftime("%Y-%m-%d")
    request.session['special_offer_id'] = offer_id
    product_obj = Product.objects.filter(LOGIN_id=request.session['shop_lid'])
    offer_obj = SpecialOffers.objects.get(id=offer_id)
    return render(request, "shop/update_special_offer.html", {'product_obj': product_obj, 'stare_date': offer_obj.StartDate,
                                                      'end_date': offer_obj.EndDate, 'offer_obj': offer_obj, 'cur_date': cur_date})


@login_required(login_url='/')
def update_special_offer_action(request):
    product_id = request.POST['select']
    start_date = request.POST['Start_date']
    end_date = request.POST['End_date']
    details = request.POST['Details']
    offer_obj = SpecialOffers.objects.get(id=request.session['special_offer_id'])
    offer_obj.StartDate = start_date
    offer_obj.EndDate = end_date
    offer_obj.Details = details
    offer_obj.PRODUCT = Product.objects.get(id=product_id)
    offer_obj.save()
    return HttpResponse('''<script>alert("Offer updated"); window.location="/manage_special_offers#about"</script>''')


@login_required(login_url='/')
def delete_special_offers(request, offer_id):
    offer_obj = SpecialOffers.objects.get(id=offer_id)
    offer_obj.delete()
    return HttpResponse('''<script>alert("Offer deleted"); window.location="/manage_special_offers#about"</script>''')


@login_required(login_url='/')
def send_complaint_view_reply(request):
    complaint_obj = ShopComplaint.objects.filter(SHOP__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/send_complaint_view_reply.html", {'complaint_obj': complaint_obj})


@login_required(login_url='/')
def complaint_search(request):
    date = request.POST['Date']
    complaint_obj = Complaint.objects.filter(SHOP__LOGIN_id=request.session['shop_lid'], Date=date)
    return render(request, "shop/send_complaint_view_reply.html", {'complaint_obj': complaint_obj, 'search': date})


@login_required(login_url='/')
def send_complaint(request):
    ob = BookingDetails.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    d = []
    for i in ob:
        d.append(i.BOOKING.id)
    obb = Booking.objects.filter(id__in=d)
    return render(request, "shop/send_complaint.html", {'obb': obb})


@login_required(login_url='/')
def send_complaint_action(request):
    user_id = request.POST['user_id']
    complaint = request.POST['Complaint']
    complaint_obj = ShopComplaint()
    complaint_obj.Complaints = complaint
    complaint_obj.Date = datetime.now()
    complaint_obj.Reply = "pending"
    complaint_obj.SHOP = Shop.objects.get(LOGIN_id=request.session['shop_lid'])
    complaint_obj.USER = User.objects.get(id=user_id)
    complaint_obj.save()
    return HttpResponse('''<script>alert("sent successfully"); window.location="/send_complaint_view_reply#about"</script>''')


@login_required(login_url='/')
def update_stock(request):
    stock_obj = Stock.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/update_stock.html", {'stock_obj': stock_obj})


@login_required(login_url='/')
def add_shop_product_stock(request):
    products = Product.objects.filter(LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/add_shop_product_stock.html", {'products': products})


@login_required(login_url='/')
def add_shop_product_stock_action(request):
    product_id = request.POST['Product_id']
    stock = request.POST['Stock']
    stock_obj = Stock()
    stock_obj.StockNo = stock
    stock_obj.PRODUCT = Product.objects.get(id=product_id)
    stock_obj.Date = datetime.today()
    stock_obj.save()
    return HttpResponse('''<script>alert("Stock added"); window.location="/update_stock#about"</script>''')


@login_required(login_url='/')
def update_shop_product_stock(request, stock_id):
    request.session['stock_id'] = stock_id
    stock_obj = Stock.objects.get(id=stock_id)
    return render(request, "shop/update_shop_product_stock.html", {'stock_obj': stock_obj})


@login_required(login_url='/')
def update_shop_stock_action(request):
    stock = request.POST['Stock']
    stock_obj = Stock.objects.get(id=request.session['stock_id'])
    stock_obj.StockNo = stock
    stock_obj.Date = datetime.today()
    stock_obj.save()
    return HttpResponse('''<script>alert("Stock updated"); window.location="/update_stock#about"</script>''')


@login_required(login_url='/')
def product_stock_search(request):
    product_search = request.POST['Product_search']
    stock_obj = Stock.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'],
                                     PRODUCT__ProductName__istartswith=product_search)
    return render(request, "shop/update_stock.html", {'stock_obj': stock_obj, 'search': product_search})


@login_required(login_url='/')
def view_booking_update_status(request):
    ob = BookingDetails.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    d = []
    for i in ob:
        d.append(i.BOOKING.id)
    obb = Booking.objects.filter(id__in=d)
    return render(request, "shop/view_booking_update_status.html", {'booking_obj': obb})


@login_required(login_url='/')
def booking_update(request, booking_id):
    request.session['booking_id'] = booking_id
    booking_obj = BookingDetails.objects.filter(BOOKING_id=booking_id)
    total_amount = 0
    for i in booking_obj:
        amount = float(i.Quantity) * float(i.PRODUCT.Price)
        total_amount = total_amount + amount
    ob = Booking.objects.get(id=booking_id)
    ob.Amount = total_amount
    ob.save()
    return render(request, "shop/booking_update.html", {'booking_obj': booking_obj, 'total_amount': total_amount})


@login_required(login_url='/')
def booking_accept(request):
    booking_obj = Booking.objects.get(id=request.session['booking_id'])
    booking_obj.Status = "accepted"
    booking_obj.save()
    return HttpResponse('''<script>alert("booking Accepted");
     window.location="/view_booking_update_status#about"</script>''')


@login_required(login_url='/')
def booking_reject(request):
    booking_obj = Booking.objects.get(id=request.session['booking_id'])
    booking_obj.Status = "rejected"
    booking_obj.save()
    return HttpResponse('''<script>alert("booking Rejected");
     window.location="/view_booking_update_status#about"</script>''')


@login_required(login_url='/')
def insert_user_details(request):
    return render(request, "shop/insert_user_details.html")


@login_required(login_url='/')
def insert_user_details1(request):
    username = request.POST['textfield']
    phone = request.POST['textfield2']
    billing_obj = Billing()
    billing_obj.SHOP = Shop.objects.get(LOGIN_id=request.session['shop_lid'])
    billing_obj.Amount = '0'
    billing_obj.Date = datetime.now()
    billing_obj.Username = username
    billing_obj.Phone = phone
    billing_obj.Status = 'pending'
    billing_obj.save()
    request.session['oid'] = billing_obj.id
    return HttpResponse('''<script>alert("Billing started");window.location='/manage_bill#about'</script>''')


@login_required(login_url='/')
def manage_bill(request):
    details_obj = BillingDetails.objects.filter(BILLING=request.session['oid'])
    old_bill = Billing.objects.get(id=request.session['oid'])
    return render(request, "shop/manage_bill.html", {'val': details_obj, 'val1': old_bill})


@login_required(login_url='/')
def finish(request):
    o = Billing.objects.get(id=request.session['oid'])
    o.status = 'finished'
    o.save()
    return HttpResponse('''<script>alert("billing finished");window.location='/insert_user_details#about'</script>''')


@login_required(login_url='/')
def add_new(request):
    stock_obj = Stock.objects.filter(PRODUCT__LOGIN_id=request.session['shop_lid'])
    return render(request, "shop/add_new.html", {'val': stock_obj})


@login_required(login_url='/')
def add_order(request, stock_id):
    stock_obj = Stock.objects.get(id=stock_id)
    request.session['stock_id'] = stock_id
    return render(request, "shop/add_order.html", {'val': stock_obj})


@login_required(login_url='/')
def add_order1(request):
    request.session['OID'] = request.session['oid']
    qty = request.POST['textfield']
    ob = Stock.objects.get(id=request.session['stock_id'])
    request.session['PRO_id'] = ob.PRODUCT.id
    tt = int(ob.PRODUCT.Price) * int(qty)
    stock_no = ob.StockNo
    nstk = int(stock_no) - int(qty)
    if nstk >= 0:
        up = Stock.objects.get(id=request.session['stock_id'])
        up.StockNo = nstk
        up.save()

        obe = Billing.objects.get(id=request.session['OID'])
        obe.Amount = int(obe.Amount)+tt
        obe.save()
        obe1 = BillingDetails()
        obe1.PRODUCT = Product.objects.get(id=request.session['PRO_id'])
        obe1.BILLING = obe
        obe1.Quantity = qty
        obe1.save()
        return HttpResponse('''<script>alert(" added to cart");window.location='/manage_bill#about'</script>''')

    else:
       return HttpResponse('''<script>alert("Out of stock");window.location='/manage_bill#about'</script>''')

    # else:
    #     return HttpResponse('''<script>alert("invalid data");window.location='/manage_bill#about'</script>''')


# ////////////////////////////////////////////// SELLER ///////////////////////////////////////////////

# def seller_register(request):
#     return render(request, "Seller/seller_registration.html")
#
#
# def seller_registration_action(request):
#     first_name = request.POST['First_name']
#     last_name = request.POST['Last_name']
#     date_of_birth = request.POST['Date_of_Birth']
#     gender = request.POST['Gender']
#     place = request.POST['Place']
#     post = request.POST['Post']
#     pin = request.POST['Pin']
#     phone = request.POST['Phone']
#     email = request.POST['Email']
#     latitude = request.POST['Latitude']
#     longitude = request.POST['Longitude']
#     status = request.POST['Status']
#     username = request.POST['Username']
#     password = request.POST['Password']
#
#     login_obj = Login()
#     login_obj.Username = username
#     login_obj.Password = password
#     login_obj.Type = "pending"
#     login_obj.save()
#
#     seller_obj = Seller()
#     seller_obj.FirstName = first_name
#     seller_obj.LastName = last_name
#     seller_obj.DOB = date_of_birth
#     seller_obj.Gender = gender
#     seller_obj.Place = place
#     seller_obj.Post = post
#     seller_obj.Pin = pin
#     seller_obj.Phone = phone
#     seller_obj.Email = email
#     seller_obj.Latitude = latitude
#     seller_obj.Longitude = longitude
#     seller_obj.Status = status
#     seller_obj.LOGIN = login_obj
#     seller_obj.save()
#     return HttpResponse('''<script>alert("successfully registered"); window.location="/"</script>''')
#
#
# def seller_home(request):
#     return render(request, "Seller/seller_home.html")
#
#
# def manage_sellers_product(request):
#     product_obj = Product.objects.filter(LOGIN_id=request.session['seller_lid'])
#     return render(request, "Seller/manage_sellers_product.html", {'product_obj': product_obj})
#
#
# def search_sellers_product(request):
#     product = request.POST['Product']
#     product_obj = Product.objects.filter(LOGIN_id=request.session['seller_lid'], ProductName__istartswith=product)
#     return render(request, "Seller/manage_sellers_product.html", {'product_obj': product_obj, 'product': product})
#
#
# def add_sellers_product(request):
#     category = Category.objects.all()
#     return render(request, "Seller/add_sellers_product.html", {'category': category})
#
#
# def add_sellers_product_action(request):
#     product_name = request.POST['Product_name']
#     details = request.POST['Details']
#     price = request.POST['Price']
#     selected_category = request.POST['selected_category']
#     image = request.FILES['Image']
#     fss = FileSystemStorage()
#     product_image = fss.save(image.name, image)
#     product_obj = Product()
#     product_obj.ProductName = product_name
#     product_obj.ProductDetails = details
#     product_obj.Price = price
#     product_obj.CATEGORY = Category.objects.get(id=selected_category)
#     product_obj.Image = product_image
#     product_obj.LOGIN = Login.objects.get(id=request.session['seller_lid'])
#     product_obj.save()
#     return HttpResponse('''<script>alert("Product added"); window.location="/manage_sellers_product"</script>''')
#
#
# def update_seller_product(request, product_id):
#     request.session['product_id'] = product_id
#     category = Category.objects.all()
#     product_obj = Product.objects.get(id=request.session['product_id'])
#     return render(request, "Seller/update_sellers_product.html", {'category': category, 'product_obj': product_obj})
#
#
# def update_sellers_product_action(request):
#     product_name = request.POST['Product_name']
#     details = request.POST['Details']
#     price = request.POST['Price']
#     selected_category = request.POST['selected_category']
#     if 'Image' in request.FILES:
#         image = request.FILES['Image']
#         fss = FileSystemStorage()
#         product_image = fss.save(image.name, image)
#         product_obj = Product.objects.get(id=request.session['product_id'])
#         product_obj.ProductName = product_name
#         product_obj.ProductDetails = details
#         product_obj.Price = price
#         product_obj.CATEGORY = Category.objects.get(id=selected_category)
#         product_obj.Image = product_image
#         product_obj.LOGIN = Login.objects.get(id=request.session['seller_lid'])
#         product_obj.save()
#         return HttpResponse('''<script>alert("Product Updated"); window.location="/manage_sellers_product"</script>''')
#     else:
#         product_obj = Product.objects.get(id=request.session['product_id'])
#         product_obj.ProductName = product_name
#         product_obj.ProductDetails = details
#         product_obj.Price = price
#         product_obj.CATEGORY = Category.objects.get(id=selected_category)
#         product_obj.LOGIN = Login.objects.get(id=request.session['seller_lid'])
#         product_obj.save()
#         return HttpResponse('''<script>alert("Product Updated"); window.location="/manage_sellers_product"</script>''')
#
#
# def delete_seller_product(request, product_id):
#     product_obj = Product.objects.get(id=product_id)
#     product_obj.delete()
#     return HttpResponse('''<script>alert("Product Deleted"); window.location="/manage_sellers_product"</script>''')


# ////////////////////////////////////////////// WEB SERVICE /////////////////////////////////////////////////


def login_code(request):
    username = request.POST['username']
    password = request.POST['password']
    try:
        login_obj = Login.objects.get(Username=username, Password=password)
        if login_obj is None:
            data = {"task": "invalid"}
            return JsonResponse(data)
        elif login_obj.Type == "user":
            request.session['user_id'] = login_obj.id
            data = {"task": "user", "id": login_obj.id}
            return JsonResponse(data)

        elif login_obj.Type == "seller":
            data = {"task": "seller", "id": login_obj.id, "type": login_obj.Type}
            return JsonResponse(data)

    except Exception as e:
        print(e)
        data = {"task": "invalid"}
        return JsonResponse(data)


def user_registration(request):
    first_name = request.POST['First_name']
    last_name = request.POST['Last_name']
    age = request.POST['Age']
    gender = request.POST['Gender']
    place = request.POST['Place']
    post_office = request.POST['Post']
    pin_code = request.POST['Pin']
    phone = request.POST['Phone']
    email_id = request.POST['Email']
    username = request.POST['Username']
    password = request.POST['Password']

    lob = Login()
    lob.Username = username
    lob.Password = password
    lob.Type = 'user'
    lob.save()

    user_obj = User()
    user_obj.FirstName = first_name
    user_obj.LastName = last_name
    user_obj.Age = age
    user_obj.Gender = gender
    user_obj.Place = place
    user_obj.Post = post_office
    user_obj.Pin = pin_code
    user_obj.Phone = phone
    user_obj.Email = email_id
    user_obj.LOGIN = lob
    user_obj.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def view_shop(request):
    shop_obj = Shop.objects.all()
    shop_data = []
    for i in shop_obj:
        data = {'shop': i.ShopName, 'place': i.Place, 'post': i.Post, 'pin': i.Pin, 'phone': i.Phone,
                'email': i.Email, 'shop_id': i.id}
        shop_data.append(data)
    r = json.dumps(shop_data)
    return HttpResponse(r)


def search_view_shop(request):
    search_shop = request.POST['search_shop']
    shop_obj = Shop.objects.filter(ShopName__istartswith=search_shop)
    shop_data = []
    for i in shop_obj:
        data = {'shop': i.ShopName, 'place': i.Place, 'post': i.Post, 'pin': i.Pin, 'phone': i.Phone,
                'email': i.Email, 'shop_id': i.id}
        shop_data.append(data)
    r = json.dumps(shop_data)
    return HttpResponse(r)


def view_offers(request):
    offer_obj = Offer.objects.all()
    offer_data = []
    for i in offer_obj:
        data = {'product': i.PRODUCT.ProductName, 'details': i.Details, 'start_date': str(i.StartDate),
                'end_date': str(i.EndDate), 'image': str(i.PRODUCT.Image.url)}
        offer_data.append(data)
    r = json.dumps(offer_data)
    return HttpResponse(r)


def search_view_offers(request):
    product_name = request.POST['search_product']
    offer_obj = Offer.objects.filter(PRODUCT__ProductName__istartswith=product_name)
    offer_data = []
    for i in offer_obj:
        data = {'product': i.PRODUCT.ProductName, 'details': i.Details, 'start_date': str(i.StartDate),
                'end_date': str(i.EndDate), 'image': str(i.PRODUCT.Image.url)}
        offer_data.append(data)
    r = json.dumps(offer_data)
    return HttpResponse(r)


def view_special_offers(request):
    offer_obj = SpecialOffers.objects.all()
    offer_data = []
    for i in offer_obj:
        data = {'product': i.PRODUCT.ProductName, 'details': i.Details, 'start_date': str(i.StartDate),
                'end_date': str(i.EndDate), 'image': str(i.PRODUCT.Image.url)}
        offer_data.append(data)
    r = json.dumps(offer_data)
    return HttpResponse(r)


def search_view_special_offers(request):
    product_name = request.POST['search_product']
    offer_obj = SpecialOffers.objects.filter(PRODUCT__ProductName__istartswith=product_name)
    offer_data = []
    for i in offer_obj:
        data = {'product': i.PRODUCT.ProductName, 'details': i.Details, 'start_date': str(i.StartDate),
                'end_date': str(i.EndDate), 'image': str(i.PRODUCT.Image.url)}
        offer_data.append(data)
    r = json.dumps(offer_data)
    return HttpResponse(r)


def view_notification_user(request):
    notification_obj = Notification.objects.all()
    notification_data = []
    for i in notification_obj:
        data = {'notification': i.Notifications, 'date': str(i.Date)}
        notification_data.append(data)
    r = json.dumps(notification_data)
    return HttpResponse(r)


def view_category_user(request):
    category_obj = Category.objects.all()
    category_data = []
    for i in category_obj:
        data = {'category': i.CategoryName, 'category_id': i.id}
        category_data.append(data)
    r = json.dumps(category_data)
    return HttpResponse(r)


def view_products_user(request):
    shop_id = request.POST['shop_id']
    shop_obj = Shop.objects.get(id=shop_id)
    category_id = request.POST['category_id']
    product_obj = Stock.objects.filter(PRODUCT__CATEGORY_id=category_id, PRODUCT__LOGIN_id=shop_obj.LOGIN.id)
    product_data = []
    for i in product_obj:
        data = {'product': i.PRODUCT.ProductName, 'details': i.PRODUCT.ProductDetails, 'price': i.PRODUCT.Price,
                'image': str(i.PRODUCT.Image.url), 'stocks': i.StockNo, 'product_id': i.PRODUCT.id}
        product_data.append(data)
    r = json.dumps(product_data)
    return HttpResponse(r)


def shop_product_rating(request):
    rating = request.POST["Rating"]
    review = request.POST["Review"]
    lid = request.POST["lid"]
    shop_id = request.POST["shop_id"]
    rating_obj = Rating()
    rating_obj.Ratings = rating
    rating_obj.Date = datetime.today()
    rating_obj.Reviews = review
    rating_obj.USER = User.objects.get(LOGIN_id=lid)
    rating_obj.SHOP = Shop.objects.get(id=shop_id)
    rating_obj.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def shop_complaints_reply(request):
    user_id = request.POST['lid']
    complaint_obj = Complaint.objects.filter(USER__LOGIN_id=user_id)
    data = []
    for i in complaint_obj:
        row = {'Complaint': i.Complaints, 'Reply': i.Reply, 'Date': str(i.Date), 'Shop_name': i.SHOP.ShopName}
        data.append(row)
    r = json.dumps(data)
    return HttpResponse(r)


def view_shop_name(request):
    shop_obj = Shop.objects.all()
    shop_data = []
    for i in shop_obj:
        data = {'shop_name': i.ShopName, 'shop_id': i.id}
        shop_data.append(data)
    r = json.dumps(shop_data)
    return HttpResponse(r)


def send_shop_complaint(request):
    complaint = request.POST["complaint"]
    login_id = request.POST["lid"]
    shop_id = request.POST["shop_id"]
    shop_obj = Shop.objects.get(id=shop_id)
    user_obj = User.objects.get(LOGIN_id=login_id)
    complaint_obj = Complaint()
    complaint_obj.Complaints = complaint
    complaint_obj.Date = datetime.now().today()
    complaint_obj.Reply = "pending"
    complaint_obj.SHOP = shop_obj
    complaint_obj.USER = user_obj
    complaint_obj.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def search_user_complaints_reply(request):
    user_id = request.POST['lid']
    search_shop_name = request.POST['shop_name']
    complaint_obj = Complaint.objects.filter(USER__LOGIN_id=user_id, SHOP__ShopName__istartswith=search_shop_name)
    data = []
    for i in complaint_obj:
        row = {'Complaint': i.Complaints, 'Reply': i.Reply, 'Date': str(i.Date), 'Shop_name': i.SHOP.ShopName}
        data.append(row)
    r = json.dumps(data)
    return HttpResponse(r)


def view_rental_products_user(request):
    category_id = request.POST['category_id']
    rental_id = request.POST['rental_id']
    product_obj = RentalProduct.objects.filter(CATEGORY_id=category_id, RENTAL_id=rental_id)
    product_data = []
    for i in product_obj:
        data = {'product': i.ProductName, 'details': i.ProductDetails, 'price': i.Price, 'image': str(i.Image.url),
                'product_id': i.id, 'stocks': i.StockNo}
        product_data.append(data)
    r = json.dumps(product_data)
    return HttpResponse(r)


def view_rental(request):
    shop_obj = Rental.objects.all()
    shop_data = []
    for i in shop_obj:
        data = {'rental': i.Name, 'place': i.Place, 'post': i.Post, 'pin': i.Pin, 'phone': i.Phone,
                'email': i.Email, 'rental_id': i.id}
        shop_data.append(data)
    r = json.dumps(shop_data)
    return HttpResponse(r)


def search_view_rental(request):
    search_rental = request.POST['search_rental']
    shop_obj = Rental.objects.filter(Name__istartswith=search_rental)
    shop_data = []
    for i in shop_obj:
        data = {'shop': i.Name, 'place': i.Place, 'post': i.Post, 'pin': i.Pin, 'phone': i.Phone,
                'email': i.Email, 'rental_id': i.id}
        shop_data.append(data)
    r = json.dumps(shop_data)
    return HttpResponse(r)


def orders(request):
    pro_id = request.POST['pid']
    rental_id = request.POST['rental_id']
    qty = request.POST['quantity']
    lid = request.POST['lid']
    from_date = request.POST['from_date']
    to_date = request.POST['to_date']
    ob = RentalProduct.objects.get(id=pro_id)
    tt = int(ob.Price) * int(qty)
    stock = ob.StockNo
    nstk = int(stock) - int(qty)
    if int(stock) >= int(qty):
        up = RentalProduct.objects.get(id=pro_id)
        up.StockNo = nstk
        up.save()

        ob = RentalBooking()
        ob.USER = User.objects.get(LOGIN__id=lid)
        ob.Status = 'order'
        ob.Date = datetime.now().today()
        ob.FromDate = from_date
        ob.ToDate = to_date
        ob.Amount = tt
        ob.save()

        obj = RentalBookingDetails()
        obj.RENTAL_BOOKING = ob
        obj.Quantity = qty
        obj.PRODUCT = RentalProduct.objects.get(id=pro_id)
        obj.save()
        id = ob.id
        data = {"task": "valid", "oid": id}
        r = json.dumps(data)
        print(r)
        return HttpResponse(r)
    else:
        data = {"task": "out of stock"}
        r = json.dumps(data)
        print(r)
        return HttpResponse(r)


def order_shop_products(request):
    print("**********************", request.POST)
    pro_id = request.POST['pid']
    qty = request.POST['quantity']
    lid = request.POST['lid']
    ob = Stock.objects.get(PRODUCT_id=pro_id)
    tt = int(ob.PRODUCT.Price) * int(qty)
    stock = ob.StockNo
    nstk = int(stock) - int(qty)
    if int(stock) >= int(qty):
        up = Stock.objects.get(PRODUCT_id=pro_id)
        up.StockNo = nstk
        up.save()

        ob = Booking()
        ob.USER = User.objects.get(LOGIN__id=lid)
        ob.Status = 'order'
        ob.Date = datetime.now().today()
        ob.Amount = tt
        ob.save()

        obj = BookingDetails()
        obj.BOOKING = ob
        obj.Quantity = qty
        obj.PRODUCT = Product.objects.get(id=pro_id)
        obj.save()
        id = ob.id
        data = {"task": "valid", "oid": id}
        r = json.dumps(data)
        print(r)
        return HttpResponse(r)
    else:
        data = {"task": "out of stock"}
        r = json.dumps(data)
        print(r)
        return HttpResponse(r)

# //////////////////////////////////////////////// SELLER ////////////////////////////////////////////////


def seller_registration(request):
    first_name = request.POST['First_name']
    last_name = request.POST['Last_name']
    age = request.POST['Age']
    gender = request.POST['Gender']
    place = request.POST['Place']
    post_office = request.POST['Post']
    pin_code = request.POST['Pin']
    phone = request.POST['Phone']
    email_id = request.POST['Email']
    username = request.POST['Username']
    password = request.POST['Password']

    lob = Login()
    lob.Username = username
    lob.Password = password
    lob.Type = 'seller'
    lob.save()

    seller_obj = Seller()
    seller_obj.FirstName = first_name
    seller_obj.LastName = last_name
    seller_obj.Age = age
    seller_obj.Gender = gender
    seller_obj.Place = place
    seller_obj.Post = post_office
    seller_obj.Pin = pin_code
    seller_obj.Phone = phone
    seller_obj.Email = email_id
    seller_obj.LOGIN = lob
    seller_obj.Status = "available"
    seller_obj.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def view_product(request):
    seller_id = request.POST['lid']
    product_obj = Product.objects.filter(LOGIN_id=seller_id)
    product_data = []
    for i in product_obj:
        data = {'Product': i.ProductName, 'Type': i.CATEGORY.CategoryName, 'Details': i.ProductDetails,
                'Price_per_day': i.Price, 'Image': str(i.Image.url), 'product_id': i.id}
        product_data.append(data)
    r = json.dumps(product_data)
    return HttpResponse(r)


def add_product(request):
    print("(((((((((((((((((((((((((", request.POST)
    user_id = request.POST['lid']
    product = request.POST["product_name"]
    type1 = request.POST["category_id"]
    details = request.POST["product_details"]
    price_per_day = request.POST["price_per_day"]
    image = request.FILES["image"]
    fss = FileSystemStorage()
    img_file = fss.save(image.name, image)
    adding = Product()
    adding.ProductName = product
    adding.CATEGORY = Category.objects.get(id=type1)
    adding.ProductDetails = details
    adding.Price = price_per_day
    adding.Image = img_file
    adding.LOGIN = Login.objects.get(id=user_id)
    adding.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def delete_product(request):
    product_id = request.POST["p_id"]
    product_obj = Product.objects.get(id=product_id)
    product_obj.delete()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def edit_product_view(request):
    product_id = request.POST["p_id"]
    product_obj = Product.objects.filter(id=product_id)
    product_data = []

    for i in product_obj:
        data = {'product': i.ProductName, 'Details': i.ProductDetails, 'Price_per_day': i.Price,
                'Image': str(i.Image.url)}
        product_data.append(data)
    # r = json.dump(product_data )
    return JsonResponse(product_data, safe=False)


def edit_product(request):
    print("%%%%%%%%%%%%%%%%%%%%%%5", request.POST)
    product_id = request.POST["p_id"]
    product_obj = Product.objects.get(id=product_id)
    product = request.POST["product_name"]
    type1 = request.POST["category_id"]
    details = request.POST["product_details"]
    price_per_day = request.POST["price_per_day"]
    image = request.FILES["image"]
    fss = FileSystemStorage()
    img_file = fss.save(image.name, image)
    product_obj.ProductName = product
    product_obj.CATEGORY = Category.objects.get(id=type1)
    product_obj.ProductDetails = details
    product_obj.Price = price_per_day
    product_obj.Image = img_file
    product_obj.save()
    data = {"task": "success"}
    # r = json.dump(data)
    return JsonResponse(data, safe=False)


def update_seller_status(request):
    seller_lid = request.POST['lid']
    status = request.POST['seller_status']
    seller_obj = Seller.objects.get(LOGIN_id=seller_lid)
    seller_obj.Status = status
    seller_obj.save()
    data = {"task": "success"}
    r = json.dumps(data)
    return HttpResponse(r)


def update_location(request):
    print("+++++++++++++++++++++++++++++++++", request.POST)
    latitude = request.POST['lat']
    longitude = request.POST['lon']
    login_id = request.POST['lid']
    try:
        location_obj = Location.objects.get(SELLER__LOGIN_id=login_id)
        location_obj.Latitude = latitude
        location_obj.Longitude = longitude
        location_obj.save()
        data = {"task": "success"}
        r = json.dumps(data)
        return HttpResponse(r)
    except:
        location_obj1 = Location()
        location_obj1.Latitude = latitude
        location_obj1.Longitude = longitude
        del_obj = Seller.objects.get(LOGIN_id=login_id)
        location_obj1.SELLER = del_obj
        location_obj1.save()
        data = {"task": "success"}
        r = json.dumps(data)
        return HttpResponse(r)
