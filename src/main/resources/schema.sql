CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to bliushtein_yp_sprint3;

CREATE TABLE IF NOT EXISTS public.posts
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    full_text text COLLATE pg_catalog."default" NOT NULL,
    created_when timestamp without time zone NOT NULL,
    owner uuid NOT NULL,
    CONSTRAINT posts_pkey PRIMARY KEY (id),
    CONSTRAINT owner_fk FOREIGN KEY (owner)
        REFERENCES public.users (id) MATCH FULL
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        DEFERRABLE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.posts
    OWNER to bliushtein_yp_sprint3;

CREATE TABLE IF NOT EXISTS public.comments
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    text text COLLATE pg_catalog."default" NOT NULL,
    created_when time without time zone NOT NULL,
    post uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT post_fk FOREIGN KEY (post)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.comments
    OWNER to bliushtein_yp_sprint3;

CREATE TABLE IF NOT EXISTS public.tags
(
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    post uuid NOT NULL,
    CONSTRAINT tags_pkey PRIMARY KEY (post, name),
    CONSTRAINT post_pk FOREIGN KEY (post)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tags
    OWNER to bliushtein_yp_sprint3;