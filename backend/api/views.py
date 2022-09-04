from django.http import HttpResponse, JsonResponse

from .models import Note


# Create your views here.
def index(request):
    return HttpResponse("API is alive! You are: TODO")


def get_all_notes(request):
    query = Note.objects.order_by('-created')
    print(Note.objects.filter(tags=2)[0].tags.all().values())
    #print(query)
    #print(query.tags.all())
    return JsonResponse({'notes': list(query.values())})


def get_note(request, note_id):
    return JsonResponse({'note_id': note_id})


def create_note(request):
    return JsonResponse({})
