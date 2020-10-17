import os
import requests
import dateutil.parser
from datetime import datetime, timezone
from dataclasses import dataclass
from dotenv import load_dotenv

load_dotenv()


@dataclass(frozen=True)
class TrelloList:
    id: str
    name: str

    @staticmethod
    def from_api_response(list_json):
        return TrelloList(id=list_json['id'], name=list_json['name'])


@dataclass(frozen=True)
class TrelloCard:
    id: str
    name: str
    lastActivity: datetime

    def days_since_last_activity(self):
        current = datetime.now(timezone.utc)
        time_since = current - self.lastActivity
        return time_since.days

    @staticmethod
    def from_api_response(list_json):
        last_activity = dateutil.parser.isoparse(list_json['dateLastActivity'])
        return TrelloCard(id=list_json['id'],
                          name=list_json['name'],
                          lastActivity=last_activity)


def fetch_board_ids():
    api_url = os.getenv('TRELLO_API_URL')
    api_key = os.getenv('TRELLO_API_KEY')
    api_token = os.getenv('TRELLO_API_TOKEN')
    resp = requests.get(f'{api_url}/1/members/me/boards',
                        params={
                            'key': api_key,
                            'token': api_token
                        })
    resp.raise_for_status()
    board_json = resp.json()
    board_ids = [board['id'] for board in board_json]
    return board_ids


def fetch_lists_in_board(board_id):
    api_url = os.getenv('TRELLO_API_URL')
    api_key = os.getenv('TRELLO_API_KEY')
    api_token = os.getenv('TRELLO_API_TOKEN')
    resp = requests.get(f'{api_url}/1/boards/{board_id}/lists',
                        params={
                            'key': api_key,
                            'token': api_token
                        })
    resp.raise_for_status()
    list_json = resp.json()
    lists = [TrelloList.from_api_response(lst) for lst in list_json]
    return lists


def fetch_cards_in_list(list_id):
    api_url = os.getenv('TRELLO_API_URL')
    api_key = os.getenv('TRELLO_API_KEY')
    api_token = os.getenv('TRELLO_API_TOKEN')
    resp = requests.get(f'{api_url}/1/lists/{list_id}/cards',
                        params={
                            'key': api_key,
                            'token': api_token
                        })
    resp.raise_for_status()
    card_json = resp.json()
    cards = [TrelloCard.from_api_response(card) for card in card_json]
    return cards


def archive_card(card_id):
    api_url = os.getenv('TRELLO_API_URL')
    api_key = os.getenv('TRELLO_API_KEY')
    api_token = os.getenv('TRELLO_API_TOKEN')
    resp = requests.put(f'{api_url}/1/cards/{card_id}',
                        params={
                            'key': api_key,
                            'token': api_token
                        },
                        json={'closed': True})

    resp.raise_for_status()


if __name__ == '__main__':
    print(fetch_cards_in_list('5d7eebc2d5c4f04795278d6c'))
