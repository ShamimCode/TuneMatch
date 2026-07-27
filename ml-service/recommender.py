import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity

from database import load_songs_df
from features import expand_audio_features, normalize_features, AUDIO_FEATURE_COLS


class ContentRecommender:
    def __init__(self):
        self.songs_df = None
        self.similarity_matrix = None
        self._build()

    def _build(self):
        raw = load_songs_df()
        expanded = expand_audio_features(raw)
        normalized = normalize_features(expanded)

        self.songs_df = normalized.reset_index(drop=True)

        feature_matrix = self.songs_df[AUDIO_FEATURE_COLS].values
        self.similarity_matrix = cosine_similarity(feature_matrix)

    def get_similar_songs(self, track_id: str, top_n: int = 10):
        if track_id not in self.songs_df["track_id"].values:
            return []

        idx = self.songs_df.index[self.songs_df["track_id"] == track_id][0]
        source_song = self.songs_df.iloc[idx]

        similarity_scores = list(enumerate(self.similarity_matrix[idx]))
        similarity_scores = sorted(similarity_scores, key=lambda x: x[1], reverse=True)

        results = []
        seen_title_artist = {(source_song["title"], source_song["artist"])}

        for i, score in similarity_scores:
            if i == idx:
                continue

            song = self.songs_df.iloc[i]
            key = (song["title"], song["artist"])

            if key in seen_title_artist:
                continue

            seen_title_artist.add(key)
            results.append(
                {
                    "track_id": song["track_id"],
                    "title": song["title"],
                    "artist": song["artist"],
                    "genre": song["genre"],
                    "similarity_score": round(float(score), 4),
                }
            )

            if len(results) >= top_n:
                break

        return results
