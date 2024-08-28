Title: Writing your own blog post
Slug: writing-blog-post
Date: 2016-10-26 19:42
Category: Meta
Tags: blog, blog post, contribution, community
Authors: Mihails Volkovs
Status: published
Summary: This blog post is about writing your own blog post. Why? Well, you want to be an author in the Test Automation community, don't you? :smile: Please open the blog post before answering that question. How? Easily, I will show you. So the next blog post could be yours.

# What is it all about?

This blog is an attempt to build a community around Test Automation. We (as a community) can share our experiences in writing testware, what works, what doesn't work, review tools and libraries developed both inside Ericsson and in the open source world, share success stories or post-mortem reports. It might be fun, it might be beneficial for you, your work or your career.

# Isn't it TAF related?

No. Not at all. TAF initiated it (and might be interested in an open feedback channel). But the community is much bigger than TAF. So there is definitely a place for competitive products, for very different topics. The blog should be driven by community.

# Why do I need my own blog post at all?

Well. There is no definitive answer. I can just list examples of other peoples' probable motivation. But it is up to you if you want to participate actively.

You may be interested in writing your own blog post because:

- you are trying to better understand a topic by educating others;
- you have been investigating a topic recently and want to summarize it (while it is still fresh in your head);
- you want to prove to yourself that you are good enough in a particular subject;
- you want to advertise your tool or library and receive open feedback from end users;
- you feel upset about repeating your points again and again to different people, so it might be better to share a link;
- you build your name/reputation as an expert in a particular topic and want acceptance from the community;
- you want to build a professional network;
- it is your hobby, you want to make friends, express yourself, etc.;
- because you will never know if you like it until you try it :smile: ;
- and a million other reasons.
 
# OK, ley me try. What should I do?

1. First of all - check out the repository (https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_doc) for blog articles.
1. Go to *ERICtaf_doc/taf-blog/content/* folder and create / commit your own .md file
1. let somebody review your work:
```bash
git push origin HEAD:refs/for/master
```

That's it!

P.S. There are [nightly](https://fem119-eiffel004.lmera.ericsson.se:8443/jenkins/view/All/job/Compile_Blog/) [jobs](https://fem119-eiffel004.lmera.ericsson.se:8443/jenkins/view/All/job/Deploy_Blog/) regenerating and redeploying static site.
You will be able to find your draft article in https://taf.seli.wh.rnd.internal.ericsson.com/blog/drafts/. If you are satisfied with how the article looks you can add the header *Status* with the value *published* to the MarkDown file, so that your blog post appears on the main page and gets published via RSS.

# Could you provide more details on file format?

The blog is powered by Pelican. MarkDown is used as article files format. You can create your blog post from the template *yyyy-mm-dd--blog_name.md* . Please follow the existing file name pattern (however, it is not required by Pelican). In the beginning of the file there are Pelican-specific headers:

1. **Title** - Human readable post title, which will be visible on the main page of the blog.
1. **Slug** is the ID of the post. It will also be visible in the URL when your blog post is opened. It should be unique. You should never modify it after your article is published (so that bookmarked links are not broken, and also the external services like the comments system use that ID to reference the article).
1. **Date** on which the blog post was created. Please use the given date format.
1. **Category** is the blog post optional meta data. It is possible to have only one category per post.
1. **Tags** is another type of blog post meta data. It is allowed to have an arbitrary number of tags.
1. **Authors** - your first and last names (and your coauthor's)
1. **Status** - by default, every article has a status **draft**, please change it to **published** after you have finalized your work (it could be done in a separate commit after the blog post has been reviewed, merged and generated in https://taf.seli.wh.rnd.internal.ericsson.com/blog/drafts/).
1. **Summary** is optional. You can describe your article in a couple of short sentences. They will be visible on the main page of the blog. If you don't have a summary, Pelican will automatically take the beginning of your post as a summary.

Then you can write your blog post content in [MarkDown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet). [GitHub MarkDown extentions](http://facelessuser.github.io/pymdown-extensions/extensions/github/) are supported, so you can use [Emojis](http://www.webpagefx.com/tools/emoji-cheat-sheet/), aligned tables, automatic links, etc. Please see [the generated example](https://taf.seli.wh.rnd.internal.ericsson.com/blog/drafts/page-id-visible-in-blog-url.html).

# Should I generate my blog post locally before publishing?

No. It is not required. You can commit the draft article and check how it was generated. 

But it might be safer (and there would be a shorter feedback loop) to generate it locally. You need Python and [Pelican](http://docs.getpelican.com/en/stable/) installed. Just run *start.bat* in taf-blog folder (or its linux alternative). It will regenerate a static site and start a web server. Open localhost:8000 to see the updated site.

# Can I subscribe to a favourite author or topics I am interested in?

Yes. The index page for each category / tag / author has *Atom feed* links in HTML header visible for RSS readers. You can also subscribe to all articles.

Index page examples: 

- https://taf.seli.wh.rnd.internal.ericsson.com/blog/category/meta.html
- https://taf.seli.wh.rnd.internal.ericsson.com/blog/tag/3rd-party-library.html
- https://taf.seli.wh.rnd.internal.ericsson.com/blog/author/gerald-glennon.html
- https://taf.seli.wh.rnd.internal.ericsson.com/blog/archives.html

