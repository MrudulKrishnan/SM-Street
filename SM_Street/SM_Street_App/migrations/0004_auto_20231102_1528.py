# Generated by Django 3.2.20 on 2023-11-02 09:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('SM_Street_App', '0003_alter_rentalbookingdetails_product'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='rentalbookingdetails',
            name='QUANTITY',
        ),
        migrations.AddField(
            model_name='rentalbookingdetails',
            name='Quantity',
            field=models.IntegerField(default=1),
            preserve_default=False,
        ),
    ]