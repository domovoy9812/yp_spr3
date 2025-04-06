--gen_random_uuid() to generate new id
delete from posts;
insert into posts (id, name, full_text, short_text, likes, image_key)
values
('ba6136ae-58c6-488a-8c18-77660307cf3c', 'first post name', 'first post full text', 'first post short text', 10, 'eaa94221-036b-4cc3-a777-7c4ee4f10d67'),
('f9440871-fa16-47f6-987f-165b994f1b27', 'second post name', 'second post full text', 'second post short text', 0, 'f9f6a2b2-d89a-4ee6-8d02-13a547d0e5c5'),
('e6b1b47c-6e33-45c3-a3f7-5e5768ab55ce', 'third post name', 'third post full text', 'third post short text', 0, 'a2e3d6dd-4425-41ed-b42a-7b6355bda6a6');

insert into tags (post, name)
values
('ba6136ae-58c6-488a-8c18-77660307cf3c', 'first tag'),
('e6b1b47c-6e33-45c3-a3f7-5e5768ab55ce', 'first tag'),
('e6b1b47c-6e33-45c3-a3f7-5e5768ab55ce', 'second tag');

insert into comments(id, text, post)
values
('65558158-7484-4868-90f4-15e72189b02d', 'first comment', 'ba6136ae-58c6-488a-8c18-77660307cf3c'),
('b0335fac-95b8-4168-aa00-ea3ac4f8adf5', 'second comment', 'f9440871-fa16-47f6-987f-165b994f1b27'),
('f0c1f0aa-8a49-4601-85da-aa18db7bb8f9', 'third comment', 'f9440871-fa16-47f6-987f-165b994f1b27');
