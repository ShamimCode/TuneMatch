import os
import mysql.connector
import pandas as pd
from dotenv import load_dotenv
from sqlalchemy import create_engine

load_dotenv()


def get_connection():
    return mysql.connector.connect(
        host=os.getenv("DB_HOST"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        database=os.getenv("DB_NAME"),
    )


def get_engine():
    user = os.getenv("DB_USER")
    password = os.getenv("DB_PASSWORD")
    host = os.getenv("DB_HOST")
    db_name = os.getenv("DB_NAME")
    connection_string = f"mysql+mysqlconnector://{user}:{password}@{host}/{db_name}"
    return create_engine(connection_string)


def load_songs_df() -> pd.DataFrame:
    """Load all songs with their audio features into a DataFrame."""
    engine = get_engine()
    query = (
        "SELECT track_id, title, artist, genre, popularity, audio_features FROM songs"
    )
    df = pd.read_sql(query, engine)
    return df


def load_interactions_df() -> pd.DataFrame:
    """Load all user interactions (likes/plays/skips)."""
    engine = get_engine()
    query = """
        SELECT user_id, track_id, interaction_type, created_at
        FROM user_interactions
    """
    df = pd.read_sql(query, engine)
    return df
