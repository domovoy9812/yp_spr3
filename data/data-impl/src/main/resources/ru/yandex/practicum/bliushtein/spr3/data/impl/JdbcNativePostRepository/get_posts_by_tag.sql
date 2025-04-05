select posts.*
from tags, posts
where tags.name = ?
    and tags.post = posts.id
order by posts.created_when desc
