package com.drynesscalculator;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import org.apache.commons.math3.distribution.BinomialDistribution;

import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

@Singleton
public class DrynessCalculatorPanel extends PluginPanel {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    JPanel calcPanel = new JPanel();

    double minPercent = 0.00;
    double maxPercent = 1000000000.00;
    double stepSizePercent = 0.01;

    int minFrac = 0;
    int maxFrac = Integer.MAX_VALUE;
    int stepSizeFrac = 1;

    JLabel dropRateLabel;
    JLabel killsLabel;
    JLabel dropCountLabel;
    JLabel onRateDropsLabel;
    JLabel onRateDrops;
    JLabel probabilityDryLabel;
    JLabel probabilityDry;
    JLabel cumulativeProbabilityDryLabel;
    JLabel cumulativeProbabilityDry;
    JPanel filler;
    JCheckBox percentFlag;
    JSpinner dropSpinner;
    JSpinner killsSpinner;
    JSpinner dropCountSpinner;

    void updatePanel(){
        removeComponents();
        addComponents();
        calcPanel.validate();
        calcPanel.repaint();
        validate();
        repaint();
    }

    void init() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10,10,10,10));

        calcPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        calcPanel.setBorder(new EmptyBorder(10,10,10,10));
        calcPanel.setLayout(new GridLayout(0,2));

        percentFlag = new JCheckBox("Percentage");
        percentFlag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePanel();
                updateRates();
            }
        });

        dropRateLabel = new JLabel("Drop Rate");
        killsLabel = new JLabel("Kills");
        dropCountLabel = new JLabel("Drops Received");
        onRateDropsLabel = new JLabel("On Rate: ");
        onRateDrops = new JLabel();
        probabilityDryLabel = new JLabel("Dry Chance: ");
        probabilityDry = new JLabel();
        cumulativeProbabilityDryLabel = new JLabel("Cumulative: ");
        cumulativeProbabilityDryLabel.setToolTipText("Probability of getting N drops or fewer.");
        cumulativeProbabilityDry = new JLabel();

        filler = new JPanel();
        filler.setBackground(ColorScheme.DARKER_GRAY_COLOR);


        calcPanel.add(percentFlag);

        calcPanel.add(filler);

        calcPanel.add(dropRateLabel);

        if(percentFlag.isSelected()) {
            SpinnerNumberModel dropRate;
            dropRate = new SpinnerNumberModel(0.00, minPercent, maxPercent, stepSizePercent);
            dropSpinner = new JSpinner(dropRate);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor)dropSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(2);
            calcPanel.add(dropSpinner);
        } else {
            SpinnerNumberModel dropRate;
            dropRate = new SpinnerNumberModel(0, minFrac, maxFrac, stepSizeFrac);
            dropSpinner = new JSpinner(dropRate);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor)dropSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(0);
            calcPanel.add(dropSpinner);
        }

        calcPanel.add(killsLabel);
        SpinnerNumberModel killCounter = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        killsSpinner = new JSpinner(killCounter);
        calcPanel.add(killsSpinner);

        calcPanel.add(dropCountLabel);
        SpinnerNumberModel dropCounterModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        dropCountSpinner = new JSpinner(dropCounterModel);
        calcPanel.add(dropCountSpinner);

        calcPanel.add(onRateDropsLabel);
        calcPanel.add(onRateDrops);
        calcPanel.add(probabilityDryLabel);
        calcPanel.add(probabilityDry);
        calcPanel.add(cumulativeProbabilityDryLabel);
        cumulativeProbabilityDryLabel.setToolTipText("Probability of getting " + dropSpinner.getValue() + " drops or fewer.");
        calcPanel.add(cumulativeProbabilityDry);

        calcPanel.setVisible(true);

        add(calcPanel);

        killsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateRates();
            }
        });

        dropSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateRates();
            }
        });

        dropCountSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateRates();
            }
        });
    }

    void addComponents() {
        if(percentFlag.isSelected()) {
            SpinnerNumberModel dropRate;
            dropRate = new SpinnerNumberModel(0.00, minPercent, maxPercent, stepSizePercent);
            dropSpinner = new JSpinner(dropRate);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor)dropSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(2);
            calcPanel.add(dropSpinner);
        } else {
            SpinnerNumberModel dropRate;
            dropRate = new SpinnerNumberModel(0, minFrac, maxFrac, stepSizeFrac);
            dropSpinner = new JSpinner(dropRate);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor)dropSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(0);
            calcPanel.add(dropSpinner);
        }

        calcPanel.add(killsLabel);
        calcPanel.add(killsSpinner);

        calcPanel.add(dropCountLabel);
        calcPanel.add(dropCountSpinner);

        calcPanel.add(onRateDropsLabel);
        calcPanel.add(onRateDrops);
        calcPanel.add(probabilityDryLabel);
        calcPanel.add(probabilityDry);
        calcPanel.add(cumulativeProbabilityDryLabel);
        calcPanel.add(cumulativeProbabilityDry);

        dropSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateRates();
            }
        });

        calcPanel.setVisible(true);
    }

    void removeComponents() {
        calcPanel.remove(dropSpinner);
        calcPanel.remove(killsLabel);
        calcPanel.remove(killsSpinner);
        calcPanel.remove(dropCountLabel);
        calcPanel.remove(dropCountSpinner);
        calcPanel.remove(onRateDrops);
        calcPanel.remove(probabilityDry);
    }

    void updateRates() {
        double dropRate;
        if(percentFlag.isSelected())
            dropRate = (double) dropSpinner.getValue();
        else {
            Integer dropRateInt = (int) dropSpinner.getValue();
            dropRate = 1.0 / dropRateInt.doubleValue();
        }

        System.out.println("Drop Rate: " + dropRate);

        Integer killsInt =(int)killsSpinner.getValue();

        double kills = killsInt.doubleValue();
        int expected = (int)Math.round(Math.floor(dropRate*kills));
        onRateDrops.setText(""+expected);

        Integer dropsInt = (int) dropCountSpinner.getValue();
        double drops = dropsInt.doubleValue();

//        double prob = Math.pow(dropRate, drops);
//        double inverseProb = Math.pow((1.0-dropRate), kills-drops);

        BinomialDistribution binomialDistribution = new BinomialDistribution(killsInt, dropRate);
        double exactDropProb = binomialDistribution.probability(dropsInt);
        double cumulativeProb = binomialDistribution.cumulativeProbability(dropsInt);
//        double binomialCoef = binomialCoefficient((int)killsSpinner.getValue(), (int)dropCountSpinner.getValue());

//        System.out.println("\n\n\nProbability: " + prob + "\nInverse Probability: " + inverseProb + "\nBinomial: " + binomialCoef);

//        double dryness = binomialCoef * prob * inverseProb;


        probabilityDry.setText("" + df2.format(exactDropProb*100.0) + "%");
        cumulativeProbabilityDry.setText("Chance of " + probabilityDry.getText() + " or fewer: ");
        cumulativeProbabilityDry.setText("" + df2.format(cumulativeProb*100.0) + "%");
    }
//
//    static long binomialCoefficient(int n, int k) {
//        long C[][] = new long[n+1][k+1];
//        int i, j;
//
//        // Calculate  value of Binomial Coefficient in bottom up manner
//        for (i = 0; i <= n; i++)
//        {
//            for (j = 0; j <= min(i, k); j++)
//            {
//                // Base Cases
//                if (j == 0 || j == i)
//                    C[i][j] = 1;
//
//                    // Calculate value using previously stored values
//                else
//                    C[i][j] = C[i-1][j-1] + C[i-1][j];
//            }
//        }
//
//        return C[n][k];
//    }
//
//    static long min(long a, long b)
//    {
//        return (a < b) ? a: b;
//    }
}

