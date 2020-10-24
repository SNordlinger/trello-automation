import logging
import trello_automation.trello as trello

logger = logging.getLogger(__name__)


def find_done_list(board_lists):
    done_lists = (lst for lst in board_lists if lst.name.lower() == 'done')
    return next(done_lists, None)


def fetch_done_cards(board_id):
    logger.info(f'Searching board with id {board_id} for "Done" list')
    board_lists = trello.fetch_lists_in_board(board_id)
    done_list = find_done_list(board_lists)
    if done_list is None:
        return []

    logger.info(f'Searching cards in "Done" list with id {board_id}')
    return trello.fetch_cards_in_list(done_list.id)


def archive_old_cards_in_board(board_id):
    done_cards = fetch_done_cards(board_id)
    old_cards = filter_old_cards(done_cards)
    for card in old_cards:
        logger.info(f'Archiving card {card.name}')
        trello.archive_card(card.id)


def archive_old_cards():
    board_ids = trello.fetch_board_ids()
    for board_id in board_ids:
        archive_old_cards_in_board(board_id)


def filter_old_cards(cards):
    return [card for card in cards if card.days_since_last_activity() > 30]
