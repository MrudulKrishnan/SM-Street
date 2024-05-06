from django.db import models

# Create your models here.


class Login(models.Model):
    Username = models.CharField(max_length=20)
    Password = models.CharField(max_length=20)
    Type = models.CharField(max_length=20)


class Shop(models.Model):
    ShopName = models.CharField(max_length=20)
    Place = models.CharField(max_length=20)
    Post = models.CharField(max_length=20)
    Pin = models.IntegerField()
    Phone = models.BigIntegerField()
    Email = models.CharField(max_length=30)
    Latitude = models.FloatField()
    Longitude = models.FloatField()
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)


class User(models.Model):
    FirstName = models.CharField(max_length=20)
    LastName = models.CharField(max_length=20)
    Age = models.IntegerField()
    Gender = models.CharField(max_length=20)
    Place = models.CharField(max_length=20)
    Post = models.CharField(max_length=20)
    Pin = models.IntegerField()
    Phone = models.BigIntegerField()
    Email = models.CharField(max_length=30)
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)


class Rating(models.Model):
    Ratings = models.CharField(max_length=20)
    Reviews = models.CharField(max_length=20)
    Date = models.DateField()
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    SHOP = models.ForeignKey(Shop, on_delete=models.CASCADE)


class Complaint(models.Model):
    Complaints = models.CharField(max_length=100)
    Date = models.DateField()
    Reply = models.CharField(max_length=100)
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    SHOP = models.ForeignKey(Shop, on_delete=models.CASCADE)


class ShopComplaint(models.Model):
    Complaints = models.CharField(max_length=100)
    Date = models.DateField()
    Reply = models.CharField(max_length=100)
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    SHOP = models.ForeignKey(Shop, on_delete=models.CASCADE)


class Category(models.Model):
    CategoryName = models.CharField(max_length=20)


class Product(models.Model):
    ProductName = models.CharField(max_length=20)
    Image = models.FileField()
    ProductDetails = models.CharField(max_length=100)
    Price = models.CharField(max_length=20)
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)
    CATEGORY = models.ForeignKey(Category, on_delete=models.CASCADE)


class Notification(models.Model):
    Notifications = models.CharField(max_length=100)
    Date = models.DateField()


class Seller(models.Model):
    FirstName = models.CharField(max_length=20)
    LastName = models.CharField(max_length=20)
    Age = models.IntegerField()
    Gender = models.CharField(max_length=20)
    Place = models.CharField(max_length=20)
    Post = models.CharField(max_length=20)
    Pin = models.IntegerField()
    Phone = models.BigIntegerField()
    Email = models.CharField(max_length=30)
    Status = models.CharField(max_length=20)
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)


class Stock(models.Model):
    StockNo = models.CharField(max_length=20)
    Date = models.DateField()
    PRODUCT = models.ForeignKey(Product, on_delete=models.CASCADE)


class Billing(models.Model):
    Amount = models.CharField(max_length=20)
    Date = models.DateField()
    Status = models.CharField(max_length=20)
    SHOP = models.ForeignKey(Shop, on_delete=models.CASCADE)
    Username = models.CharField(max_length=30)
    Phone = models.BigIntegerField()


class Offer(models.Model):
    StartDate = models.DateField()
    EndDate = models.DateField()
    Details = models.CharField(max_length=20)
    PRODUCT = models.ForeignKey(Product, on_delete=models.CASCADE)


class Booking(models.Model):
    Status = models.CharField(max_length=20)
    Date = models.DateField()
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    Amount = models.CharField(max_length=20)


class BookingDetails(models.Model):
    BOOKING = models.ForeignKey(Booking, on_delete=models.CASCADE)
    PRODUCT = models.ForeignKey(Product, on_delete=models.CASCADE)
    Quantity = models.IntegerField()


class SpecialOffers(models.Model):
    StartDate = models.DateField()
    EndDate = models.DateField()
    Details = models.CharField(max_length=20)
    PRODUCT = models.ForeignKey(Product, on_delete=models.CASCADE)


class Rental(models.Model):
    Name = models.CharField(max_length=20)
    Place = models.CharField(max_length=20)
    Post = models.CharField(max_length=20)
    Pin = models.IntegerField()
    Phone = models.BigIntegerField()
    Email = models.CharField(max_length=30)
    Latitude = models.FloatField()
    Longitude = models.FloatField()
    LOGIN = models.ForeignKey(Login, on_delete=models.CASCADE)


class RentalProduct(models.Model):
    ProductName = models.CharField(max_length=20)
    Image = models.FileField()
    ProductDetails = models.CharField(max_length=100)
    Price = models.CharField(max_length=20)
    RENTAL = models.ForeignKey(Rental, on_delete=models.CASCADE)
    CATEGORY = models.ForeignKey(Category, on_delete=models.CASCADE)
    StockNo = models.IntegerField()


class RentalBooking(models.Model):
    USER = models.ForeignKey(User, on_delete=models.CASCADE)
    Status = models.CharField(max_length=20)
    Date = models.DateField()
    FromDate = models.DateField()
    ToDate = models.DateField()
    Amount = models.CharField(max_length=20)


class BillingDetails(models.Model):
    BILLING = models.ForeignKey(Billing, on_delete=models.CASCADE)
    PRODUCT = models.ForeignKey(Product, on_delete=models.CASCADE)
    Quantity = models.CharField(max_length=20)


class RentalBookingDetails(models.Model):
    RENTAL_BOOKING = models.ForeignKey(RentalBooking, on_delete=models.CASCADE)
    PRODUCT = models.ForeignKey(RentalProduct, on_delete=models.CASCADE)
    Quantity = models.IntegerField()


class Location(models.Model):
    Latitude = models.FloatField()
    Longitude = models.FloatField()
    SELLER = models.ForeignKey(Seller, on_delete=models.CASCADE)
