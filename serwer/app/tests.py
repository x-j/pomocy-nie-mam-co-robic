from django.test import TestCase, Client
from django.contrib.auth.models import User

from app.views import find_windows
from app.models import Block

import json
import arrow


class GlobalTestCase(TestCase):

    def test_register(self):
        User.objects.create_user(username='janek')

        client = Client()

        response = client.post('/register/',{
            "nickname": "janek"
        })
        result = response.json()
        self.assertFalse(result["result"])

        response = client.post('/register/',{
            "name": "asdf asdf",
            "password": "pass",
            "nickname": "asdf"
        })
        result = response.json()
        self.assertTrue(result["result"])

        self.assertTrue(
            User.objects.filter(username="asdf").exists()
        )

    def test_login(self):
        User.objects.create_user(username='jozef', password="joestar")

        client = Client()

        response = client.post('/login/',{
            "nickname": "jozef",
            "password": "jp2"
        })
        result = response.json()
        self.assertFalse(result["result"])

        response = client.post('/login/',{
            "nickname": "jozef",
            "password": "joestar"
        })
        result = response.json()
        self.assertTrue(result["result"])

        response = client.post('/login/',{
            "nickname": "100percentcoverage",
            "password": "tbc"
        })
        result = response.json()
        self.assertFalse(result["result"])

    def test_post(self):

        client = Client()

        json_data = json.dumps({})
        response = client.post('/save_timetable/', {'event_list': json_data})

class WindowsTestCase(TestCase):

    def setUp(self):

        user1 = User.objects.create_user("u1")
        user2 = User.objects.create_user("u2")

        Block.objects.create(
            person=user1,
            start=arrow.get("2016-05-11 13:00:00").datetime,
            end=arrow.get("2016-05-11 15:00:00").datetime
        )

        Block.objects.create(
            person=user1,
            start=arrow.get("2016-05-11 18:00:00").datetime,
            end=arrow.get("2016-05-11 19:00:00").datetime
        )


        Block.objects.create(
            person=user2,
            start=arrow.get("2016-05-11 12:00:00").datetime,
            end=arrow.get("2016-05-11 14:00:00").datetime
        )

        Block.objects.create(
            person=user2,
            start=arrow.get("2016-05-11 17:00:00").datetime,
            end=arrow.get("2016-05-11 19:00:00").datetime
        )

    def test_find_windows(self):
        windows = find_windows("u1", "u2")

        self.assertEqual(
            windows[0]["start"], arrow.get("2016-05-11 15:00:00").datetime
        )
        self.assertEqual(
            windows[0]["end"], arrow.get("2016-05-11 17:00:00").datetime
        )

    def test_get_windows(self):

        client = Client()

        args = {"user1": "u1", "user2": "u2"}

        response = client.get("/get_windows/", args)
        result = response.json()

        self.assertEqual(
            result["windows"],
            [{
                "start": "2016-05-11T15:00:00Z",
                "end": "2016-05-11T17:00:00Z"
            }]
        )