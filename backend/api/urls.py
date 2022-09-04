from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('notes/all/', views.get_all_notes, name='get_all_notes'),
    path('notes/<int:note_id>/', views.get_note, name='get_note'),
    path('notes/', views.get_note, name='create_note'),
]
