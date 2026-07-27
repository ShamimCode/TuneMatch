import json
import pandas as pd

AUDIO_FEATURE_COLS = [
    "danceability",
    "energy",
    "key",
    "loudness",
    "mode",
    "speechiness",
    "acousticness",
    "instrumentalness",
    "liveness",
    "valence",
    "tempo",
    "time_signature",
]


def expand_audio_features(df: pd.DataFrame) -> pd.DataFrame:
    """
    Takes a DataFrame with an 'audio_features' JSON string column
    and expands it into separate numeric columns.
    """
    df = df.copy()

    features_expanded = df["audio_features"].apply(json.loads).apply(pd.Series)

    df = pd.concat([df.drop(columns=["audio_features"]), features_expanded], axis=1)

    return df


def normalize_features(df: pd.DataFrame, feature_cols: list = None) -> pd.DataFrame:
    """
    Min-max normalizes numeric feature columns to a 0-1 range,
    since cosine similarity is sensitive to differing scales
    (e.g. tempo ranges ~60-200, but valence ranges 0-1).
    """
    if feature_cols is None:
        feature_cols = AUDIO_FEATURE_COLS

    df = df.copy()
    for col in feature_cols:
        min_val = df[col].min()
        max_val = df[col].max()
        if max_val > min_val:
            df[col] = (df[col] - min_val) / (max_val - min_val)
        else:
            df[col] = 0.0

    return df
