# Generated by Django 3.2.20 on 2023-11-11 04:36

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('SM_Street_App', '0006_delete_location'),
    ]

    operations = [
        migrations.AlterField(
            model_name='seller',
            name='Phone',
            field=models.BigIntegerField(),
        ),
    ]
