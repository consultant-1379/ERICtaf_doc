#!/usr/bin/env python
# -*- coding: utf-8 -*- #
from __future__ import unicode_literals

# This file is only used if you use `make publish` or
# explicitly specify it as your config file.

#import os
#import sys
#sys.path.append(os.curdir)
#from pelicanconf import *

AUTHOR = 'TAF Team'
SITENAME = 'Test Automation'
SITEURL = 'https://taf.seli.wh.rnd.internal.ericsson.com/blog/'
RELATIVE_URLS = False
DELETE_OUTPUT_DIRECTORY = True

PATH = 'content'

TIMEZONE = 'Europe/Dublin'
LOCALE = ('eni',# On Windows
    'en_IE'		# On Unix/Linux
    )
DEFAULT_LANG = 'en'

DEFAULT_METADATA = {
    'status': 'draft',
}

# Feed generation
FEED_ALL_ATOM = 'feeds/all.atom.xml'
CATEGORY_FEED_ATOM = 'feeds/cat.%s.atom.xml'
AUTHOR_FEED_ATOM = 'feeds/auth.%s.atom.xml'
TAG_FEED_ATOM = 'feeds/tag.%s.atom.xml'
TRANSLATION_FEED_ATOM = None
FEED_MAX_ITEMS = 40

# Blogroll
LINKS = (
         ('TAF Home', 'https://taf.seli.wh.rnd.internal.ericsson.com/'),
         ('TAF Documentation', 'https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html'),
         ('TE Documentation', 'https://taf.seli.wh.rnd.internal.ericsson.com/tedocs/latest/index.html'),
         ('TMS Application', 'https://taftm.lmera.ericsson.se/#tm'),
         ('TMS Documentation', 'https://taftm.lmera.ericsson.se/#help/app/tm'),
         ('TAF Workshop Exercises', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_koans'),
         ('Common TAF operators', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.nms/taf-tor-operators'),
         ('Common UI components', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cds/uisdk-composite-components'),
        )

# Social widget
SOCIAL = (
          ('TAF Blog', 'https://taf.seli.wh.rnd.internal.ericsson.com/blog/'),
          ('TAF StackOverflow', 'https://taf-stackoverflow.seli.wh.rnd.internal.ericsson.com//'),
          ('TAF Support Board', 'https://jira-oss.seli.wh.rnd.internal.ericsson.com/secure/RapidBoard.jspa?rapidView=2587&projectKey=CIS'),
          ('User Mail List', 'https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/TAF/TAF-Users+Mail+List'),
          ('TAF Repo', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_util'),
          ('Doc Repo', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_doc'),
          ('TMS Repo', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_tm'),
          ('TDM Repo', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk.taf.testdatamanagement/ERICtaf_tdm'),
          ('Test Executor Repo', 'https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_te'),
         )

THEME = u'theme'

DEFAULT_PAGINATION = 10

# MarkDown Extensions
MD_EXTENSIONS = ['pymdownx.github(no_nl2br=True)', 'markdown.extensions.codehilite', 'markdown.extensions.toc', 'pymdownx.inlinehilite']