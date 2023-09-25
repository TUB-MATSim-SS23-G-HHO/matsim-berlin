import pandas as pd

scenario_base = "../../../../scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_base/berlin-v5.5-1pct.output_trips.csv"
scenario_half_circle = "../../../../scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_HalfCircleOutput/berlin-v5.5-1pct.output_trips.csv"
scenario_full_circle = "../../../../scenarios/berlin-v5.5-1pct/output-berlin-v5.5-1pct_FullCircleOutput/berlin-v5.5-1pct.output_trips.csv"

df1 = pd.read_csv(scenario_base, delimiter=';', low_memory=False)
df2 = pd.read_csv(scenario_half_circle, delimiter=';', low_memory=False)
df3 = pd.read_csv(scenario_full_circle, delimiter=';', low_memory=False)

merged_df_base_half = df1[['person', 'trip_id', 'main_mode']].merge(
    df2[['person', 'trip_id', 'main_mode']],
    on=['person', 'trip_id'],
    suffixes=('_base', '_half_circle')
)

merged_df_base_full = df1[['person', 'trip_id', 'main_mode']].merge(
    df3[['person', 'trip_id', 'main_mode']],
    on=['person', 'trip_id'],
    suffixes=('_base', '_full_circle')
)

merged_df_base_half.to_csv("./merged_trips_base_half.csv", index=False)
merged_df_base_full.to_csv("./merged_trips_base_full.csv", index=False)
