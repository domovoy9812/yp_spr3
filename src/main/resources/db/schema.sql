-- Table: public.binary_storage

-- DROP TABLE IF EXISTS public.binary_storage;

CREATE TABLE IF NOT EXISTS public.binary_storage
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    data bytea NOT NULL,
    CONSTRAINT binary_storage_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.binary_storage
    OWNER to bliushtein_yp_sprint4;

-- Table: public.posts

-- DROP TABLE IF EXISTS public.posts;

CREATE TABLE IF NOT EXISTS public.posts
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    full_text text COLLATE pg_catalog."default" NOT NULL,
    created_when timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    likes integer NOT NULL DEFAULT 0,
    short_text text COLLATE pg_catalog."default" NOT NULL,
    image_key uuid,
    CONSTRAINT posts_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.posts
    OWNER to bliushtein_yp_sprint4;

-- Table: public.comments

-- DROP TABLE IF EXISTS public.comments;

CREATE TABLE IF NOT EXISTS public.comments
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    text text COLLATE pg_catalog."default" NOT NULL,
    created_when time with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    post uuid NOT NULL,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT post_fk FOREIGN KEY (post)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.comments
    OWNER to bliushtein_yp_sprint4;
-- Index: fki_post_fk

-- DROP INDEX IF EXISTS public.fki_post_fk;

CREATE INDEX IF NOT EXISTS fki_post_fk
    ON public.comments USING btree
    (post ASC NULLS LAST)
    TABLESPACE pg_default;
-- Table: public.tags

-- DROP TABLE IF EXISTS public.tags;

CREATE TABLE IF NOT EXISTS public.tags
(
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    post uuid NOT NULL,
    CONSTRAINT tags_pkey PRIMARY KEY (post, name),
    CONSTRAINT post_fk FOREIGN KEY (post)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tags
    OWNER to bliushtein_yp_sprint4;