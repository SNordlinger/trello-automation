import trello_automation.archive as archive


def archive_cards(event, context):
    archive.archive_old_cards()
