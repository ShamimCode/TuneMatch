
import json
import os
from pathlib import Path

import mysql.connector
import pandas as pd
from dotenv import load_dotenv

load_dotenv()

SCRIPT_DIR = Path(__file__).resolve().parent
CSV_PATH = '../data/tunematch_songs.csv'

AUDIO_FEATURE_COLS = [
    "danceability", "energy", "key", "loudness", "mode",
    "speechiness", "acousticness", "instrumentalness",
    "liveness", "valence", "tempo", "time_signature",
]


def build_audio_features_json(row: pd.Series) -> str:
    features = {col: row[col] for col in AUDIO_FEATURE_COLS}
    return json.dumps(features)


def main():
    df = pd.read_csv(CSV_PATH)
    print(f"Loaded {len(df)} rows from {CSV_PATH}")

    conn = mysql.connector.connect(
        host=os.getenv("DB_HOST"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        database=os.getenv("DB_NAME"),
    )
    cursor = conn.cursor()

    insert_query = """
        INSERT INTO songs (track_id, title, artist, genre, duration_ms, popularity, audio_features)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            title = VALUES(title),
            artist = VALUES(artist),
            genre = VALUES(genre),
            duration_ms = VALUES(duration_ms),
            popularity = VALUES(popularity),
            audio_features = VALUES(audio_features)
    """

    rows_inserted = 0
    batch = []
    batch_size = 500

    for _, row in df.iterrows():
        audio_features_json = build_audio_features_json(row)
        batch.append((
            row["track_id"],
            row["track_name"],
            row["artists"],
            row["track_genre"],
            int(row["duration_ms"]),
            int(row["popularity"]),
            audio_features_json,
        ))

        if len(batch) >= batch_size:
            cursor.executemany(insert_query, batch)
            conn.commit()
            rows_inserted += len(batch)
            print(f"Inserted {rows_inserted} rows so far...")
            batch = []

    if batch:
        cursor.executemany(insert_query, batch)
        conn.commit()
        rows_inserted += len(batch)

    print(f"Done. Total rows inserted/updated: {rows_inserted}")

    cursor.close()
    conn.close()


if __name__ == "__main__":
    main()
