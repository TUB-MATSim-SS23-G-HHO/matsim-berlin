library(tidyverse)
data_base <- read.csv("VehicleStayPerLink_base.csv")
data_link_base <- data_base%>%group_by(link_id)%>%summarise(max_tt_base=max(stay_time))

data_half_cycle <- read.csv("VehicleStayPerLink_HalfCircle.csv")
data_link_hcircle <- data_half_cycle%>%group_by(link_id)%>%summarise(max_tt_hcircle=max(stay_time))

data_full_cycle <- read.csv("VehicleStayPerLink_FullCircle.csv")
data_link_fcircle <- data_full_cycle%>%group_by(link_id)%>%summarise(max_tt_fcircle=max(stay_time))


base_hcircle <- merge(data_link_base,data_link_hcircle, by= "link_id")
base_hcircle_change <- subset(base_hcircle, base_hcircle$max_tt_base!=base_hcircle$max_tt_hcircle)

tt_increase_afhcircle <- subset(base_hcircle_change, base_hcircle_change$max_tt_base<base_hcircle_change$max_tt_hcircle)
write.csv(tt_increase_afhcircle,"tt_increase_afhcircle.csv")


# Assuming df is your data frame
# Add a new column for the gap between max_base and max_hhalf
tt_increase_afhcircle$gap <- (tt_increase_afhcircle$max_tt_hcircle - tt_increase_afhcircle$max_tt_base)
tt_increase_afhcircle <- subset(tt_increase_afhcircle, tt_increase_afhcircle$gap>0)
# Order the data frame by the gap in descending order
sorted_tt_increase_afhcircle <- tt_increase_afhcircle[order(-tt_increase_afhcircle$gap), ]
sorted_tt_increase_afhcircle <- subset(sorted_tt_increase_afhcircle,sorted_tt_increase_afhcircle$max_tt_base>0)
# Select the top 20 links with the highest gap
top_20_links <- head(sorted_tt_increase_afhcircle, 20)
write.csv(top_20_links, "top_20_links_tt_inc_ahc.csv")

tt_decrease_afhcircle <- subset(base_hcircle_change, base_hcircle_change$max_tt_base>base_hcircle_change$max_tt_hcircle)
write.csv(tt_decrease_afhcircle,"tt_decrease_afhcircle.csv")


# Assuming df is your data frame
# Add a new column for the gap between max_base and max_hhalf
tt_decrease_afhcircle$gap <- (tt_decrease_afhcircle$max_tt_base  - tt_decrease_afhcircle$max_tt_hcircle)
tt_decreease_afhcircle <- subset(tt_decrease_afhcircle, tt_decrease_afhcircle$gap>0)
# Order the data frame by the gap in descending order
sorted_tt_decreease_afhcircle <- tt_decreease_afhcircle[order(-tt_decreease_afhcircle$gap), ]
sorted_tt_decreease_afhcircle <- subset(sorted_tt_decreease_afhcircle,sorted_tt_decreease_afhcircle$max_tt_hcircle>0)
# Select the top 20 links with the highest gap
top_20_links <- head(sorted_tt_decreease_afhcircle, 20)
write.csv(top_20_links, "top_20_links_tt_dec_ahc.csv")

# congestion after full circle





data_base <- read.csv("VehicleStayPerLink_base.csv")
data_link_base <- data_base%>%group_by(link_id)%>%summarise(max_tt_base=max(stay_time))

data_half_cycle <- read.csv("VehicleStayPerLink_HalfCircle.csv")
data_link_hcircle <- data_half_cycle%>%group_by(link_id)%>%summarise(max_tt_hcircle=max(stay_time))

data_full_cycle <- read.csv("VehicleStayPerLink_FullCircle.csv")
data_link_fcircle <- data_full_cycle%>%group_by(link_id)%>%summarise(max_tt_fcircle=max(stay_time))


base_fcircle <- merge(data_link_base,data_link_fcircle, by= "link_id")
base_fcircle_change <- subset(base_fcircle, base_fcircle$max_tt_base!=base_fcircle$max_tt_fcircle)

tt_increase_affcircle <- subset(base_fcircle_change, base_fcircle_change$max_tt_base<base_fcircle_change$max_tt_fcircle)
write.csv(tt_increase_affcircle,"tt_increase_affcircle.csv")


# Assuming df is your data frame
# Add a new column for the gap between max_base and max_hhalf
tt_increase_affcircle$gap <- (tt_increase_affcircle$max_tt_fcircle - tt_increase_affcircle$max_tt_base)
tt_increase_affcircle <- subset(tt_increase_affcircle, tt_increase_affcircle$gap>0)
# Order the data frame by the gap in descending order
sorted_tt_increase_affcircle <- tt_increase_affcircle[order(-tt_increase_affcircle$gap), ]
sorted_tt_increase_affcircle <- subset(sorted_tt_increase_affcircle,sorted_tt_increase_affcircle$max_tt_base>0)
# Select the top 20 links with the highest gap
top_20_links <- head(sorted_tt_increase_affcircle, 20)
write.csv(top_20_links, "top_20_links_tt_inc_afc.csv")

tt_decrease_affcircle <- subset(base_fcircle_change, base_fcircle_change$max_tt_base>base_fcircle_change$max_tt_fcircle)
write.csv(tt_decrease_affcircle,"tt_decrease_affcircle.csv")


# Assuming df is your data frame
# Add a new column for the gap between max_base and max_hhalf
tt_decrease_affcircle$gap <- (tt_decrease_affcircle$max_tt_base  - tt_decrease_affcircle$max_tt_fcircle)
tt_decreease_affcircle <- subset(tt_decrease_affcircle, tt_decrease_affcircle$gap>0)
# Order the data frame by the gap in descending order
sorted_tt_decreease_affcircle <- tt_decreease_affcircle[order(-tt_decreease_affcircle$gap), ]
sorted_tt_decreease_affcircle <- subset(sorted_tt_decreease_affcircle,sorted_tt_decreease_affcircle$max_tt_fcircle>0)
# Select the top 20 links with the highest gap
top_20_links <- head(sorted_tt_decreease_affcircle, 20)
write.csv(top_20_links, "top_20_links_tt_dec_afc.csv")
