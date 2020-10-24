import logging
import os
import trello_automation.archive as archive

log_level = os.getenv('LOG_LEVEL', logging.INFO)
logging.getLogger().setLevel(log_level)


def archive_cards(event, context):
    archive.archive_old_cards()
