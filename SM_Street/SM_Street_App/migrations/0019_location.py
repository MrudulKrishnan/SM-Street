# Generated by Django 3.2.20 on 2023-11-17 11:38

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('SM_Street_App', '0018_auto_20231117_1252'),
    ]

    operations = [
        migrations.CreateModel(
            name='Location',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('Latitude', models.FloatField()),
                ('Longitude', models.FloatField()),
                ('SELLER', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='SM_Street_App.seller')),
            ],
        ),
    ]