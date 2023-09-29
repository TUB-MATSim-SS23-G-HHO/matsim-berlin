data_full <- read.csv("merged_trips_base_half.csv")
# data_full <- read.csv("merged_trips_base_full.csv")
data_full <- subset(data_full,data_full$main_mode_base!="freight")
car     <- subset(data_full,data_full$main_mode_base=="car")
bicycle <- subset(data_full,data_full$main_mode_base=="bicycle")
pt      <- subset(data_full,data_full$main_mode_base=="pt")
ride    <- subset(data_full,data_full$main_mode_base=="ride")
walk    <- subset(data_full,data_full$main_mode_base=="walk")

car_full <- data.frame(table(car$main_mode_full_circle))
car_full$per <- car_full$Freq/sum(car_full$Freq)

bicycle_full <- data.frame(table(bicycle$main_mode_full_circle))
bicycle_full$per <- bicycle_full$Freq/sum(bicycle_full$Freq)

pt_full     <- data.frame(table(pt$main_mode_full_circle))
pt_full$per <- pt_full$Freq/sum(pt_full$Freq)

ride_full <- data.frame(table(ride$main_mode_full_circle))
ride_full$per <- ride_full$Freq/sum(ride_full$Freq)

library(dplyr)
library(networkD3)
library(tidyr)
library(webshot)

rm(list = ls())


# read in inertia sankey dataset
refresults <- read.csv("Modal_shift_distance.csv", header = T)
names(refresults) <- c("pc_mode","ec_mode", "frequency", "fraction" )
head(refresults)


# create nodes dataframe

pc_modes <- unique(as.character(refresults$pc_mode))
ec_modes <- unique(as.character(refresults$ec_mode))

nodes <- data.frame(node = c(0:9),name = c(pc_modes,ec_modes))
head(nodes)

#create links dataframe

refresults <- merge(refresults, nodes, by.x = "ec_mode", by.y = "name")
refresults <- merge(refresults, nodes, by.x = "pc_mode", by.y = "name")
links <- refresults[ , c("node.x", "node.y", "fraction")]
colnames(links) <- c("target", "source", "value")
head(links)

#draw sankey network
# Add a 'group' column to each connection:
links$group <- as.factor(c("type_a","type_a","type_a","type_a","type_a",
                           "type_b","type_b","type_b","type_b","type_b",
                           "type_c","type_c","type_c","type_c","type_c",
                           "type_d","type_d","type_d","type_d","type_d",
                           "type_e","type_e","type_e","type_e","type_e"))

links <- links[,c(2,1,3,4)]
# Add a 'group' column to each node. 
nodes$group <- as.factor(c("type_c","type_b","type_e","type_d","type_a",
                           "type_a","type_b","type_c","type_d","type_e"))

# Give a color for each group: NMT, ODPV, ONline,PV, SV, node
my_color <- 'd3.scaleOrdinal() .domain(["type_a", "type_b", "type_c", "type_d", "type_e"])
.range(["#EC7063","#7FB3D5","#8E44AD","#F1C40F","#808000","#73C6B6", "red","blue", "green])'

m <- sankeyNetwork(Links = links, Nodes = nodes, Source = 'source', 
                   Target = 'target', Value = 'value', NodeID = 'name',
                   units = '',fontSize = 22,fontFamily = "sans-serif",
                   LinkGroup="group",NodeGroup="group", iterations=32,
                   nodeWidth = 25, nodePadding = 20,margin= NULL)
htmlwidgets::saveWidget(m, file="mode_shift_halfcircle.html")
# htmlwidgets::saveWidget(m, file="mode_shift_fullcircle.html")
webshot::webshot("mode_shift_fullcircle.html", "mode_shift_halfcircle.html.png")
# webshot::webshot("mode_shift_fullcircle.html", "mode_shift_fullcircle.html.png")
